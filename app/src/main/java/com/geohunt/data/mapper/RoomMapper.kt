package com.geohunt.data.mapper

import com.geohunt.data.dto.room.RoomDto
import com.geohunt.data.dto.room.RoomInfoDto
import com.geohunt.data.dto.room.RoomPlayersDto
import com.geohunt.data.dto.room.RoomRoundDto
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Room
import com.geohunt.domain.model.RoomInfo
import com.geohunt.domain.model.Round

object RoomMapper {
    fun RoomDto.toModel() = Room(
        info = info.toModel(),
        players = players.map { (key, value) -> value.toModel() },
        rounds = rounds.map { (key, value) -> value.toModel() }
    )
    fun RoomInfoDto.toModel() = RoomInfo(
        status = status,
        roomCode = roomCode,
        hostId = hostId,
        totalRounds = totalRounds,
        createdAt = createdAt,
        durationPerRound = durationPerRound

    )

    fun RoomPlayersDto.toModel() = Player(
        uid = uid,
        username = username,
        joinedAt = joinedAt,
        online = online,
        ready = ready
    )

    fun RoomRoundDto.toModel() = Round(
        endedAt = endedAt,
        status = status,
        startedAt = startedAt,
        trueLat = trueLat,
        trueLng = trueLng,
        photoUrl = photoUrl,
        answers = answers.map { (key, value) -> value.toModel() }
    )
}