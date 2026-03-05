package com.geohunt.presentation.map.mp.game.vm

import com.geohunt.core.base.BaseViewModel
import com.geohunt.data.dto.room.RoomAnswersDto
import com.geohunt.domain.usecase.CalculatePointUseCase
import com.geohunt.domain.usecase.CountDistanceUseCase
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.StoreAnswerUseCase
import com.geohunt.presentation.map.mp.game.contract.GameMapMpEffect
import com.geohunt.presentation.map.mp.game.contract.GameMapMpPickerEffect
import com.geohunt.presentation.map.mp.game.contract.GameMapMpPickerIntent
import com.geohunt.presentation.map.mp.game.contract.GameMapMpPickerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameMapMpPickerVm @Inject constructor(
    private val storeAnswerUseCase: StoreAnswerUseCase,
    private val calculatePointUseCase: CalculatePointUseCase,
    private val countDistanceUseCase: CountDistanceUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
)
    : BaseViewModel<GameMapMpPickerIntent, GameMapMpPickerUiState, GameMapMpPickerEffect>(
        initialState = GameMapMpPickerUiState()) {
    val userData = getUserDataUseCase()
    override suspend fun handleIntent(intent: GameMapMpPickerIntent) {
        when (intent) {
            is GameMapMpPickerIntent.OnSaveCameraPosState -> {
                updateState { copy(cameraPositionState = intent.cameraState) }
            }

            is GameMapMpPickerIntent.OnSaveLatLng -> {
                updateState { copy(latLng = intent.latLng) }
            }

            is GameMapMpPickerIntent.OnSubmitAnswer -> {
                storeAnswer(intent.trueLoc, intent.currentRound)
            }
        }
    }

    fun storeAnswer(trueLoc: Pair<String, String>, currentRound: Int
    ) {
        val guessedLoc = state.value.latLng!!.latitude.toString() to state.value.latLng!!.longitude.toString()
        val distance = countDistanceUseCase(trueLoc, guessedLoc)
        val answer = RoomAnswersDto(
            lat = guessedLoc.first,
            lng = guessedLoc.second,
            distance = distance,
            point = calculatePointUseCase(distance)
        )
        launchWithResult(
            request = { storeAnswerUseCase(answer, currentRound, userData.userId) },
            onSuccess = {
                updateState { copy(isSuccessSubmit = true) }
            }
        )
    }

    override fun onHideLoading() {
        super.onHideLoading()
        updateState { copy(isLoadingSubmit = false) }
    }

    override fun onShowLoading() {
        super.onShowLoading()
        updateState { copy(isLoadingSubmit = true) }
    }

    override fun onHandleErrorMessage(message: String) {
        super.onHandleErrorMessage(message)
        sendEffect(GameMapMpPickerEffect.OnshowToast(message))
    }
}