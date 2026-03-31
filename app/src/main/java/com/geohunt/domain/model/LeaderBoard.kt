package com.geohunt.domain.model

data class LeaderBoard(
    val player: Player,
    val totalPoint: Int,
    val rank: Int?

)