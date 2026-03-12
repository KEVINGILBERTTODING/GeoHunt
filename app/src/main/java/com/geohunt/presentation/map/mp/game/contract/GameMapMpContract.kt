package com.geohunt.presentation.map.mp.game.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.data.dto.user.User
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Room
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.CameraPositionState.Companion.invoke

sealed class GameMapMpIntent: MviIntent {
    data class UpdateUserLoadPanorama(val status: Boolean): GameMapMpIntent()
    object OnBackPressed: GameMapMpIntent()
    object OnStartTime: GameMapMpIntent()
    data class OnSaveLatLng(val latLng: LatLng?): GameMapMpIntent()
    data class OnSaveCameraPosState(val cameraState: CameraPositionState): GameMapMpIntent()
    data class OnSubmitAnswer(val trueLoc: Pair<String, String>): GameMapMpIntent()
    object OnSubmitState: GameMapMpIntent()
}

sealed class GameMapMpEffect: MviEffect {
    data class ShowToast(val message: String): GameMapMpEffect()
    object OnBack: GameMapMpEffect()
    object OnTimeUp: GameMapMpEffect()
    object OnNavigateToResult: GameMapMpEffect()
}

data class GameMapMpUiState(
    val isLoading: Boolean = false,
    val roomData: Room = Room(),
    val endTime: Long = 0,
    val timeLeft: Int = 0,
    val isLoadingBack: Boolean = false,
    val gameMapMpState: GameMapState = GameMapState.LoadingStreetView,
    val isSuccessLoadStreetView: Boolean = false,
    val userData: User = User("", ""),
    val isHost: Boolean = false,
    val latLng : LatLng? = null,
    val cameraPositionState: CameraPositionState = CameraPositionState(),
    val isLoadingSubmit: Boolean = false,
    val isSubmit: Boolean = false,
    val isRoundStarted: Boolean = false
): MviState

sealed class GameMapState {
    object LoadingStreetView: GameMapState()
    object WaitingPlayer : GameMapState()
    object Ready : GameMapState()
}


