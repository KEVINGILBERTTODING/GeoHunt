package com.geohunt.presentation.home.event

sealed class RoomFormEvent {
    object NavigateToRoom: RoomFormEvent()
    data class ShowToastEvent(val message: String): RoomFormEvent()
    object OnSubmit: RoomFormEvent()
}