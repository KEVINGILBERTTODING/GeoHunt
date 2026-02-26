package com.geohunt.domain.usecase

import com.geohunt.domain.repository.RoomRepository
import javax.inject.Inject

class CreateRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val generateRoomCode: GenerateRoomCode
) {
    suspend operator fun invoke(totalRounds: Int, durationPerRound: Int, countryId: Int): Result<String> {
        val uid = getUserDataUseCase().userId
        val username = getUserDataUseCase().username
        val roomCode = generateRoomCode()
        if (uid.isBlank() || username.isBlank() || roomCode.isBlank()) return Result.failure(Exception("Can't create room"))
        return roomRepository.createRoom(roomCode, uid, totalRounds, durationPerRound, username, countryId)
    }
}