package com.geohunt.presentation.map.singlePlayer.result.event

sealed class GameResultSinglePlayerEvent {
    object NavigateToHome: GameResultSinglePlayerEvent()
    data class ChangeMarkerState(val trueLoc: Pair<String, String>, val guessLoc: Pair<String, String>): GameResultSinglePlayerEvent()
    object NextRound: GameResultSinglePlayerEvent()
}
