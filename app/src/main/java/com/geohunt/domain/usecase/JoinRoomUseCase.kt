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
            roomRepository.joinRoom(roomCode, getUserDataUseCase().userId, getUserDataUseCase().username)
        }catch (e: Exception) {
            Timber.d(e)
            Result.failure(e)
        }
    }
}