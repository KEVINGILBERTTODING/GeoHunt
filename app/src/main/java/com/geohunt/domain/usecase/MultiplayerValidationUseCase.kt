package com.geohunt.domain.usecase

import com.geohunt.domain.model.Room
import javax.inject.Inject

class MultiplayerValidationUseCase @Inject constructor() {
    operator fun invoke(room: Room?): Result<Unit> {
        if (room == null) return Result.failure(Exception("Room not found"))
        if (room.players.size < 2) return Result.failure(Exception("Not enough players"))
        if (room.players.any { it.ready.not() }) return Result.failure(Exception("Not all players are ready"))
        if (room.players.any { it.online.not() }) return Result.failure(Exception("Not all players are online"))
        return Result.success(Unit)
    }
}