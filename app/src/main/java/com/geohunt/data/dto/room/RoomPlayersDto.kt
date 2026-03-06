package com.geohunt.data.dto.room

data class RoomPlayersDto(
    val uid: String = "",
    val username: String = "",
    val ready: Boolean = false,
    val online: Boolean = false,
    val joinedAt: Long = 0,
    val loadPanorama : Boolean = false,
    val playerColor: Int = 0
)
