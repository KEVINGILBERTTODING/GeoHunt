package com.geohunt.data.dto.room

data class RoomRoundDto(
    val status: String = "waiting",
    val startedAt: Long = 0,
    val endedAt: Long = 0,
    val trueLat: String = "",
    val trueLng: String = "",
    val answers: HashMap<String, RoomAnswersDto> = hashMapOf()
)
