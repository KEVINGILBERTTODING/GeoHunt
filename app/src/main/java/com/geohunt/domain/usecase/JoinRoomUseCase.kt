package com.geohunt.domain.usecase

import com.geohunt.domain.repository.RoomRepository
import timber.log.Timber
import javax.inject.Inject

class JoinRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
    private val getUserDataUseCase: GetUserDataUseCase
) {
    suspend operator fun invoke(roomCode: String): Result<Unit> {
        return try {
            val roomData = roomRepository.getRoomData()
            val userData = getUserDataUseCase()
            if (roomData.isSuccess) {
                val players = roomData.getOrThrow().players.toList()
                val isPlayerAlreadyJoin = players.any { it.second.uid == userData.userId }
                if (isPlayerAlreadyJoin || players.count() <= 10) {
                    roomRepository.joinRoom(roomCode, userData.userId, userData.username)
                }else {
                    Result.failure(Exception("Room is full"))
                }
            }else {
                Result.failure(Exception(roomData.exceptionOrNull()?.message ?: "Something went wrong"))
            }
        }catch (e: Exception) {
            Timber.d(e)
            Result.failure(e)
        }
    }
}