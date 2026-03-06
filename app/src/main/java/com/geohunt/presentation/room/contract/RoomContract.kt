package com.geohunt.presentation.room.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.domain.model.Room

data class RoomUiState(
    val isLoading: Boolean = false,
    val room: Room = Room(),
    val error: String? = "",
    val isLoadingStartGame: Boolean = false,
    val isLoadingBack : Boolean = false
): MviState

sealed class RoomIntent: MviIntent {
    object LoadRoomData : RoomIntent()
    object OnStartGame : RoomIntent()
    data class OnPlayerReady(val isReady: Boolean): RoomIntent()
    object OnBack : RoomIntent()
}

sealed class RoomEffect: MviEffect {
    data class ShowToast(val message: String): RoomEffect()
    data class NavigateToGame(val id: String): RoomEffect()
    object OnBack : RoomEffect()
    object StartGame : RoomEffect()
}
