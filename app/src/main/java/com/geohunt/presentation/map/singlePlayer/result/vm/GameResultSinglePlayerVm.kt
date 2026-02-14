package com.geohunt.presentation.map.singlePlayer.result.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.presentation.map.singlePlayer.result.event.GameResultSinglePlayerEvent
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GameResultSinglePlayerVm @Inject constructor(
): ViewModel() {
    val gameEvent = MutableSharedFlow<GameResultSinglePlayerEvent>()
    val cameraPositionState = MutableStateFlow(CameraPositionState())




    fun navigateToHome() {
        viewModelScope.launch {
            gameEvent.emit(GameResultSinglePlayerEvent.NavigateToHome)
        }
    }

    fun setCameraState(state: CameraPositionState) {
        cameraPositionState.value = state
    }

}