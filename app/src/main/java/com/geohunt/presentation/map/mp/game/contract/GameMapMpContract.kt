package com.geohunt.presentation.map.mp.game.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.domain.model.Room

sealed class GameMapMpIntent: MviIntent {
    object OnSubmit : GameMapMpIntent()
    data class UpdateUserLoadPanorama(val status: Boolean): GameMapMpIntent()
    object OnBackPressed: GameMapMpIntent()
}

sealed class GameMapMpEffect: MviEffect {
    data class ShowToast(val message: String): GameMapMpEffect()
    object OnBack: GameMapMpEffect()
}

data class GameMapMpUiState(
    val isLoading: Boolean = false,
    val errorMsg: String? = "",
    val roomData: Room = Room()
): MviState
