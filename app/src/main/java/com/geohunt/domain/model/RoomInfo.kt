package com.geohunt.domain.model

data class RoomInfo(
    val roomCode: String = "",
    val status: String = "waiting",
    val hostId: String = "",
    val totalRounds: Int = 1,
    val createdAt: Long = 0,
    val durationPerRound: Int = 30, // in second
    val currentRound: Int = 0
)
