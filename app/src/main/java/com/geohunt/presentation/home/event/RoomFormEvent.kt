package com.geohunt.presentation.home.event

sealed class RoomFormEvent {
    data class NavigateToRoom(val id: String): RoomFormEvent()
    data class ShowToastEvent(val message: String): RoomFormEvent()
    object OnSubmit: RoomFormEvent()
}