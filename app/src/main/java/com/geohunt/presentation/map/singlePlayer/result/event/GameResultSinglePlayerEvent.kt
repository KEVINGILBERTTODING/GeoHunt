package com.geohunt.presentation.map.singlePlayer.result.event

sealed class GameResultSinglePlayerEvent {
    object NavigateToHome: GameResultSinglePlayerEvent()
}