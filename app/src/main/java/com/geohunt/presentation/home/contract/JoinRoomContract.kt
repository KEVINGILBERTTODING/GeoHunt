package com.geohunt.presentation.home.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState

sealed class JoinRoomIntent: MviIntent {
    object OnSubmit : JoinRoomIntent()
    data class OnRoomCodeChanged(val value: String): JoinRoomIntent()
}

sealed class JoinRoomEffect: MviEffect {
    data class ShowToast(val message: String): JoinRoomEffect()
    data class NavigateToRoom(val id: String): JoinRoomEffect()
}

data class JoinRoomUiState(
    val isLoading: Boolean = false,
    val errorMsg: String? = "",
    val roomCode: String = "",
): MviState
