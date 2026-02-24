package com.geohunt.presentation.home.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState

sealed class CreateRoomIntent: MviIntent {
    object OnSubmit : CreateRoomIntent()
    data class OnTotalRoundChanged(val value: String): CreateRoomIntent()
    data class OnDurationRoundChanged(val value: String): CreateRoomIntent()
}

sealed class CreateRoomEffect: MviEffect {
    data class ShowToast(val message: String): CreateRoomEffect()
    data class NavigateToRoom(val id: String): CreateRoomEffect()
}

data class CreateRoomUiState(
    val isLoading: Boolean = false,
    val errorMsg: String? = "",
    val totalRounds: String = "",
    val durationPerRound: String = "",
): MviState
