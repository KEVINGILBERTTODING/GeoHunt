package com.geohunt.presentation.map.singlePlayer.result.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.domain.usecase.CountDistanceUseCase
import com.geohunt.presentation.map.singlePlayer.result.event.GameResultSinglePlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameResultSinglePlayerVm @Inject constructor(
    private val countDistanceUseCase: CountDistanceUseCase
): ViewModel() {
    val gameEvent = MutableSharedFlow<GameResultSinglePlayerEvent>()
    val trueLocationState = MutableStateFlow(Pair("", ""))
    val guessedLocationState = MutableStateFlow(Pair("", ""))

    fun navigateToHome() {
        viewModelScope.launch {
            gameEvent.emit(GameResultSinglePlayerEvent.NavigateToHome)
        }
    }

    fun getLevelZoom(trueLocation: Pair<String, String>, guessedLocation: Pair<String, String>): Float {
        val distance = countDistanceUseCase(trueLocation,  guessedLocation)
        val zoomLevel = when {
            distance < 500 -> 18f
            distance < 2000 -> 15f
            distance < 10000 -> 12f
            distance < 50000 -> 10f
            else -> 2f
        }
        return zoomLevel
    }

    fun setTrueLocationState(data: Pair<String, String>) {
        trueLocationState.value = data
    }

    fun setGuessedLocationState(data: Pair<String, String>) {
        guessedLocationState.value = data
    }

    fun changeMarkerStateEvent(trueLoc: Pair<String, String>, guessLoc: Pair<String, String>) {
        viewModelScope.launch {
            gameEvent.emit(GameResultSinglePlayerEvent.ChangeMarkerState(trueLoc, guessLoc))
        }
    }

}