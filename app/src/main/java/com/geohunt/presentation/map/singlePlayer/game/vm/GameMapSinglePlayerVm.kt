package com.geohunt.presentation.map.singlePlayer.game.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.presentation.map.singlePlayer.game.event.GameMapSinglePlayerEvent
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameMapSinglePlayerVm @Inject constructor() : ViewModel() {
    private val _mapGameEvent = MutableSharedFlow<GameMapSinglePlayerEvent>()
    val mapGameEvent = _mapGameEvent.asSharedFlow()

    private val _markerPositionState = MutableStateFlow<LatLng?>(null)
    val markerPositionState = _markerPositionState.asStateFlow()

    private val _cameraPositionState = MutableStateFlow<CameraPositionState>(CameraPositionState())
    val cameraPositionState = _cameraPositionState.asStateFlow()

    fun showBottomSheetMapPickerEvent() {
        viewModelScope.launch {
            _mapGameEvent.emit(GameMapSinglePlayerEvent.ShowMapPicker)
        }
    }

    fun hideBottomSheetMapPickerEvent() {
        viewModelScope.launch {
            _mapGameEvent.emit(GameMapSinglePlayerEvent.HideMapPicker)
        }
    }

    fun errorLoadStreetViewEvent() {
        viewModelScope.launch {
            _mapGameEvent.emit(GameMapSinglePlayerEvent.ErrorLoadStreetView)
        }
    }


    fun setMarkerPositionState(latLng: LatLng) {
        _markerPositionState.value = latLng
    }

    fun setCameraPositionState(cameraPositionState: CameraPositionState) {
        _cameraPositionState.value = cameraPositionState
    }

    fun setOnBackPressedEvent() {
        viewModelScope.launch {
            _mapGameEvent.emit(GameMapSinglePlayerEvent.OnBackPressedEvent)
        }
    }
}