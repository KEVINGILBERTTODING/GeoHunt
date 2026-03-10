package com.geohunt.presentation.map.mp.result.component

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.data.dto.user.User
import com.geohunt.domain.model.Room
import com.google.maps.android.compose.CameraPositionState

data class GameResultMpUiState(
    val isLoading: Boolean = false,
    val room: Room = Room(),
    val error: String? = "",
    val isLoadingStartGame: Boolean = false,
    val isLoadingBack : Boolean = false,
    val isHost: Boolean = false,
    val userData: User = User("", ""),
    val cameraPositionState: CameraPositionState = CameraPositionState(),
): MviState

sealed class GameResultMpIntent: MviIntent {
    object OnStartGame : GameResultMpIntent()
    object OnBack : GameResultMpIntent()
}

sealed class GameResultMpEffect: MviEffect {
    data class ShowToast(val message: String): GameResultMpEffect()
    data class NavigateToGame(val id: String): GameResultMpEffect()
    object OnBack : GameResultMpEffect()
    object StartGame : GameResultMpEffect()
    object OnCancelGame : GameResultMpEffect()
}