package com.geohunt.presentation.map.mp.game.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.domain.model.Room

sealed class GameMapMpIntent: MviIntent {
    data class UpdateUserLoadPanorama(val status: Boolean): GameMapMpIntent()
    object OnBackPressed: GameMapMpIntent()
    object OnStartTime: GameMapMpIntent()
}

sealed class GameMapMpEffect: MviEffect {
    data class ShowToast(val message: String): GameMapMpEffect()
    object OnBack: GameMapMpEffect()
    object OnTimeUp: GameMapMpEffect()
}

data class GameMapMpUiState(
    val isLoading: Boolean = false,
    val roomData: Room = Room(),
    val endTime: Long = 0,
    val timeLeft: Int = 0,
    val isLoadingBack: Boolean = false,
): MviState
