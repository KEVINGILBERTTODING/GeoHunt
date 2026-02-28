package com.geohunt.domain.usecase

import com.geohunt.domain.model.Room
import com.geohunt.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRoomDataUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    operator fun invoke(): Flow<Result<Room>>{
        return roomRepository.observeRoomData()
    }
}