package com.geohunt.presentation.home.state

data class RoomFormState(
    val totalRounds: String = "",
    val totalRoundsError: String? = null,
    val durationPerRound: String = "",
    val durationPerRoundError: String? = null
)
