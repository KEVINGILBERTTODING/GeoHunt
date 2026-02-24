package com.geohunt.presentation.home.event

sealed class JoinRoomEvent {
    data class ShowToast(val message: String): JoinRoomEvent()
    object Submit: JoinRoomEvent()
    object NavigateToRoom: JoinRoomEvent()

}