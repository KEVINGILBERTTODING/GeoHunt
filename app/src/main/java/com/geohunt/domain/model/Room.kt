package com.geohunt.domain.model

data class Room(
    val info: RoomInfo = RoomInfo(),
    val players: List<Player> = listOf(),
    val rounds: List<Round> = listOf()
)
