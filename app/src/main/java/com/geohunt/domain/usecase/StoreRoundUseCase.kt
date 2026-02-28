package com.geohunt.domain.usecase

import com.geohunt.data.dto.room.RoomRoundDto
import com.geohunt.domain.repository.RoomRepository
import javax.inject.Inject

class StoreRoundUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(round: RoomRoundDto, roundNumber: Int)
    : Result<Unit>
    {
        return roomRepository.setRound(round, roundNumber)
    }
}