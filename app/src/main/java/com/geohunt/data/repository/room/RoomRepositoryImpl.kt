package com.geohunt.data.repository.room

import androidx.compose.animation.core.snap
import com.geohunt.data.dto.room.RoomDto
import com.geohunt.data.dto.room.RoomInfoDto
import com.geohunt.data.dto.room.RoomPlayersDto
import com.geohunt.data.dto.room.RoomRoundDto
import com.geohunt.data.mapper.RoomMapper.toModel
import com.geohunt.di.qualifier.ApplicationScope
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Room
import com.geohunt.domain.repository.RoomRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    @ApplicationScope private val appScope: CoroutineScope
): RoomRepository {
    private var roomFlow: SharedFlow<Result<Room>>? = null
    private var roomCodes = ""

    override suspend fun createRoom(
        roomCode: String,
        hostId: String,
        totalRounds: Int,
        durationPerRound: Int,
        username: String,
        countryId: Int
    ): Result<String> {
         return try {
             // reset flow
             roomFlow = null
             roomCodes = roomCode
            val roomRef = firebaseDatabase.getReference("rooms").child(roomCode)
            val roomInfo = RoomInfoDto(
                roomCode = roomCode,
                createdAt  = System.currentTimeMillis(),
                hostId = hostId,
                totalRounds = totalRounds,
                durationPerRound = durationPerRound,
                countryId = countryId
            )
            val roomPlayersDto = RoomPlayersDto(
                uid = hostId,
                username = username,
                joinedAt = System.currentTimeMillis(),
                online = true,
                ready = true)

            roomRef.onDisconnect().removeValue()
            roomRef.child("info").setValue(roomInfo).await()
            roomRef.child("players").child(hostId).setValue(roomPlayersDto).await()
             Result.success(roomCode)
        }catch (e: Exception) {
            Timber.d(e)
            Result.failure(Exception("Something went wrong"))
        }
    }

    override suspend fun isRoomExist(): Boolean {
        val roomRef = firebaseDatabase.getReference("rooms").child(roomCodes)
        val snapshot = roomRef.get().await()
        return snapshot.exists()
    }

    override suspend fun joinRoom(
        roomCode: String,
        uid: String,
        username: String
    ): Result<Unit> {
        // reset flow
        roomFlow = null
        roomCodes = roomCode
        return try {
            if (isRoomExist()) {
                val roomPlayersDto = RoomPlayersDto(
                    uid = uid,
                    username = username,
                    joinedAt = System.currentTimeMillis(),
                    online = true,
                    ready = true)
                val roomRef = firebaseDatabase.getReference("rooms").child(roomCode)
                roomRef.child("players").child(uid).setValue(roomPlayersDto).await()
                roomRef.child("players").child(uid).onDisconnect()
                    .setValue(roomPlayersDto.copy(online = false))
                Result.success(Unit)
            }else {
                Result.failure(Exception("Room not found"))
            }
        }catch (e: Exception) {
            Timber.d(e)
            Result.failure(Exception("Something went wrong"))
        }

    }

    override suspend fun getRoomData(): Result<RoomDto> {
        try {
            val roomRef = firebaseDatabase.getReference("rooms").child(roomCodes)
            val snapshot = roomRef.get().await()
            val roomData = snapshot.getValue<RoomDto>()
            roomData?.let {
                return Result.success(it)
            }
            return Result.failure(Exception("Room not found"))
        }catch (e: Exception) {
            Timber.d(e)
            return Result.failure(Exception("Something went wrong"))
        }
    }

    override fun observeRoomData(): Flow<Result<Room>> {
        return roomFlow ?: callbackFlow {
            val roomRef = firebaseDatabase.getReference("rooms").child(roomCodes)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val roomData = snapshot.getValue<RoomDto>()?.toModel()
                    if (roomData != null) {
                        trySend(Result.success(roomData))
                    } else {
                        trySend(Result.failure(Exception("Room not found")))
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Timber.d(error.message)
                    trySend(Result.failure(Exception("Something went wrong")))
                }
            }
            roomRef.addValueEventListener(listener)
            awaitClose {
                roomRef.removeEventListener(listener)
                roomFlow = null
            }
        }.shareIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        ).also { roomFlow = it }
    }

    override suspend fun storeRound(hashMap: HashMap<String, Any>): Result<Unit> {
        try {
            firebaseDatabase.getReference("rooms").child(roomCodes)
                .child("rounds").updateChildren(hashMap).await()
            return Result.success(Unit)
        }catch (e: Exception) {
            Timber.e(e)
            return Result.failure(Exception(e))
        }
    }


    override suspend fun deleteRoom(): Result<Unit> {
        try {
            firebaseDatabase.getReference("rooms").child(roomCodes).removeValue()
            return Result.success(Unit)
        }catch (e: Exception) {
            Timber.e(e)
            return Result.failure(Exception(e))
        }
    }

    override suspend fun updatePlayerData(hashMap: HashMap<String, Any>): Result<Unit> {
        try {
            firebaseDatabase.getReference("rooms").child(roomCodes)
                .child("players").updateChildren(hashMap).await()
            return Result.success(Unit)
        }catch (e: Exception) {
            Timber.e(e)
            return Result.failure(Exception(e))
        }
    }

}