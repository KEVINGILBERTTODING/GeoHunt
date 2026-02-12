package com.geohunt.presentation.map.singlePlayer.game.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.presentation.map.singlePlayer.game.event.GameMapSinglePlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameMapSinglePlayerVm @Inject constructor() : ViewModel() {
    private val _mapGameEvent = MutableSharedFlow<GameMapSinglePlayerEvent>()
    val mapGameEvent = _mapGameEvent.asSharedFlow()

    fun showBottomSheetMapPicker() {
        viewModelScope.launch {
            _mapGameEvent.emit(GameMapSinglePlayerEvent.showMapPicker)
        }
    }
}