package com.geohunt.domain.usecase

import com.geohunt.data.dto.room.RoomPlayersDto
import com.geohunt.domain.repository.RoomRepository
import javax.inject.Inject

class UpdatePlayerUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(playerDto: RoomPlayersDto): Result<Unit> {
        return roomRepository.updatePlayerData(playerDto)
    }
}