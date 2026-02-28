package com.geohunt.domain.usecase

import com.geohunt.domain.repository.RoomRepository
import javax.inject.Inject

class UpdatePlayerUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(hashMap: HashMap<String, Any>): Result<Unit> {
        return roomRepository.updatePlayerData(hashMap)
    }
}