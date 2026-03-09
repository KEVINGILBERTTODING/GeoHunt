package com.geohunt.domain.usecase

import com.geohunt.domain.model.Room
import javax.inject.Inject

class CheckMinimumPlayerUseCase @Inject constructor() {
    operator fun invoke(room: Room): Result<Unit> {
        val onlineCount = room.players.count { it.online }
        room.rounds.lastOrNull()?.let {
            if (onlineCount == 1) {
                return Result.failure(Exception("Not enough players, stopping..."))
            }
        }
        return Result.success(Unit)
    }
}