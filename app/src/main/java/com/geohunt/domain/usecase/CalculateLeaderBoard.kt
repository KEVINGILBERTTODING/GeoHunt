package com.geohunt.domain.usecase

import com.geohunt.domain.model.LeaderBoard
import com.geohunt.domain.model.Room
import javax.inject.Inject

class CalculateLeaderBoard @Inject constructor() {
    operator fun invoke(room: Room): List<LeaderBoard> {
        val playerPoints = room.players.map { p ->
            val total = room.rounds.sumOf { round ->
                round.answers.filter { it.uid == p.uid }.sumOf { it.point }
            }
            p to total
        }.sortedByDescending { it.second }

        val allSamePoint = playerPoints.map { it.second }.distinct().size == 1

        // Buat map point -> rank
        val pointRankMap = playerPoints
            .map { it.second }
            .distinct()
            .mapIndexed { index, point -> point to index + 1 }
            .toMap()

        return playerPoints.map { (player, point) ->
            LeaderBoard(
                player = player,
                totalPoint = point,
                rank = if (allSamePoint) null else pointRankMap[point]
            )
        }
    }
}