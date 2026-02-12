package com.geohunt.presentation.map.singlePlayer.game.event

sealed class GameMapSinglePlayerEvent {
    object ShowMapPicker: GameMapSinglePlayerEvent()
    object HideMapPicker: GameMapSinglePlayerEvent()
    object ErrorLoadStreetView: GameMapSinglePlayerEvent()
}