package com.geohunt.data.mapper

import com.geohunt.data.dto.room.RoomAnswersDto
import com.geohunt.domain.model.Answer
import kotlin.math.ln

fun RoomAnswersDto.toModel() = Answer(
    uid = uid,
    point = point,
    lat = lat,
    lng = lng,
    distance = distance,
    )