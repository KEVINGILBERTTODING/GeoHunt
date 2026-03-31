package com.geohunt.domain.usecase

import com.geohunt.domain.model.Room
import com.geohunt.domain.repository.RoomRepository
import javax.inject.Inject

class GetRoomDataUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(): Result<Room> = roomRepository.getRoomData()
}