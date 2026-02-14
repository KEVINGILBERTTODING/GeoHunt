package com.geohunt.presentation.map.singlePlayer.result.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.core.resource.Resource
import com.geohunt.domain.usecase.CalculateScoreUseCase
import com.geohunt.domain.usecase.CountDistanceUseCase
import com.geohunt.domain.usecase.DistancePrettierUseCase
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
    private val countDistanceUseCase: CountDistanceUseCase,
    private val calculateScorelUseCase: CalculateScoreUseCase,
    private val prettierUseCase: DistancePrettierUseCase
): ViewModel() {
    val totalScore = MutableStateFlow(0)
    val totalDistance = MutableStateFlow(0f)
    val totalDistancePrettier = MutableStateFlow("")
    val resultState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val gameEvent = MutableSharedFlow<GameResultSinglePlayerEvent>()
    val cameraPositionState = MutableStateFlow(CameraPositionState())


    fun setResultState(resource: Resource<Unit>) {
        resultState.value = resource
    }

    fun countDistanceAndScore(trueLoc: Pair<String, String>,
                              guessedLoc: Pair<String, String>) {
        setTotalDistance(trueLoc, guessedLoc)
        setTotalScore()
    }

    fun setTotalDistance(trueLoc: Pair<String, String>,
                         guessedLoc: Pair<String, String>) {
        totalDistance.value = countDistanceUseCase(trueLoc, guessedLoc)
        totalDistancePrettier.value = prettierUseCase(totalDistance.value)

    }

    fun setTotalScore() {
        totalScore.value = calculateScorelUseCase(totalDistance.value)
        Timber.d("total score ${totalScore.value}")
    }

    fun navigateToHome() {
        viewModelScope.launch {
            gameEvent.emit(GameResultSinglePlayerEvent.NavigateToHome)
        }
    }

    fun setCameraState(state: CameraPositionState) {
        cameraPositionState.value = state
    }

}