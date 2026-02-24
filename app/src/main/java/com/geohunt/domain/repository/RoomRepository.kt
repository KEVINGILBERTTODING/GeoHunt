package com.geohunt.domain.repository

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.room.RoomDto
import com.geohunt.domain.model.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(roomCode: String, hostId: String,
                           totalRounds: Int, durationPerRound: Int, username: String): Result<String>
    suspend fun isRoomExist(roomCode: String): Boolean
    suspend fun joinRoom(roomCode: String, uid: String, username: String): Result<Unit>
    suspend fun getRoomData(roomCode: String): Result<RoomDto>
    fun observeRoomData(roomCode: String): Flow<Result<Room>>
}