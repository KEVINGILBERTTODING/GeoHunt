package com.geohunt.domain.model

import com.geohunt.data.dto.room.RoomAnswersDto

data class Round (
    val status: String = "waiting",
    val startedAt: Long = 0,
    val endedAt: Long = 0,
    val trueLat: String = "",
    val trueLng: String = "",
    val photoUrl: String = "",
    val answers: List<Answer> = listOf()
)