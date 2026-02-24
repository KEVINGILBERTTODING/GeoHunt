package com.geohunt.domain.model

import com.geohunt.data.dto.room.RoomPlayersDto
import com.geohunt.data.dto.room.RoomRoundDto

data class Room(
    val info: RoomInfo = RoomInfo(),
    val players: List<Player> = listOf(),
    val rounds: List<Round> = listOf()
)
