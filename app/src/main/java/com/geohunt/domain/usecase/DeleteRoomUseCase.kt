package com.geohunt.domain.usecase

import com.geohunt.domain.repository.RoomRepository
import javax.inject.Inject

class DeleteRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return roomRepository.deleteRoom()
    }
}