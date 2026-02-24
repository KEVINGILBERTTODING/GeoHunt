package com.geohunt.data.dto.room

data class RoomPlayersDto(
    val uid: String = "",
    val username: String = "",
    val isReady: Boolean = false,
    val isConnected: Boolean = false,
    val joinedAt: Long = 0
)
