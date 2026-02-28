package com.geohunt.domain.repository

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.room.RoomDto
import com.geohunt.data.dto.room.RoomPlayersDto
import com.geohunt.data.dto.room.RoomRoundDto
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Room
import com.geohunt.domain.model.Round
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(roomCode: String, hostId: String,
                           totalRounds: Int, durationPerRound: Int, username: String, countryId: Int): Result<String>
    suspend fun isRoomExist(): Boolean
    suspend fun joinRoom(roomCode: String, uid: String, username: String): Result<Unit>
    suspend fun getRoomData(): Result<RoomDto>
    fun observeRoomData(): Flow<Result<Room>>
    suspend fun setRound(round: RoomRoundDto, roundNumber: Int): Result<Unit>
    suspend fun deleteRoom(): Result<Unit>
    suspend fun updatePlayerData(hashMap: HashMap<String, Any>): Result<Unit>
}