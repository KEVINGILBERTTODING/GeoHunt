package com.geohunt.presentation.map.mp.game.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.domain.model.Room
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

sealed class GameMapMpPickerIntent: MviIntent {
    data class OnSaveLatLng(val latLng: LatLng?): GameMapMpPickerIntent()
    data class OnSaveCameraPosState(val cameraState: CameraPositionState): GameMapMpPickerIntent()
    data class OnSubmitAnswer(val trueLoc: Pair<String, String>, val currentRound: Int): GameMapMpPickerIntent()
}

sealed class GameMapMpPickerEffect: MviEffect {
    object OnBack: GameMapMpPickerEffect()
    data class OnshowToast(val msg: String): GameMapMpPickerEffect()
}

data class GameMapMpPickerUiState(
    val latLng : LatLng? = null,
    val cameraPositionState: CameraPositionState = CameraPositionState(),
    val isLoadingSubmit: Boolean = false,
    val isSuccessSubmit: Boolean = false
): MviState
