package com.geohunt.data.dto.room

data class RoomDto(
    val info: RoomInfoDto = RoomInfoDto(),
    val players: HashMap<String, RoomPlayersDto> = hashMapOf(),
    val rounds: HashMap<String, RoomRoundDto> = hashMapOf()
)