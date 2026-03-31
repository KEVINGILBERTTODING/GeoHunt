package com.geohunt.presentation.map.mp.game.vm

import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.data.dto.room.RoomAnswersDto
import com.geohunt.domain.usecase.CalculatePointUseCase
import com.geohunt.domain.usecase.CheckMinimumPlayerUseCase
import com.geohunt.domain.usecase.CountDistanceUseCase
import com.geohunt.domain.usecase.DeleteRoomUseCase
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.ObserveRoomDataUseCase
import com.geohunt.domain.usecase.StoreAnswerUseCase
import com.geohunt.domain.usecase.UpdatePlayerUseCase
import com.geohunt.presentation.map.mp.game.contract.GameMapMpEffect
import com.geohunt.presentation.map.mp.game.contract.GameMapMpIntent
import com.geohunt.presentation.map.mp.game.contract.GameMapMpUiState
import com.geohunt.presentation.map.mp.game.contract.GameMapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GameMapMpVm @Inject constructor(
    private val updatePlayerUseCase: UpdatePlayerUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val observeRoomDataUseCase: ObserveRoomDataUseCase,
    private val removeRoomUseCase: DeleteRoomUseCase,
    private val checkMinimumPlayerUseCase: CheckMinimumPlayerUseCase,
    private val storeAnswerUseCase: StoreAnswerUseCase,
    private val calculatePointUseCase: CalculatePointUseCase,
    private val countDistanceUseCase: CountDistanceUseCase,
): BaseViewModel<GameMapMpIntent, GameMapMpUiState, GameMapMpEffect>(
    initialState = GameMapMpUiState()
) {

    init {
        updateState { copy(userData = getUserDataUseCase()) }
        viewModelScope.launch {
            onShowLoading()
            observeRoomDataUseCase().collect { result ->
                onHideLoading()
                when {
                    result.isSuccess -> {
                        val room = result.getOrThrow()
                        updateState {
                            copy(
                                isLoading = false,
                                roomData = room,
                                isHost = room.info.hostId == userData.userId
                            )
                        }

                        checkMinimumPlayerUseCase.invoke(room)
                            .onFailure {
                                sendEffect(GameMapMpEffect.ShowToast(it.message ?: "Something went wrong"))
                                sendEffect(GameMapMpEffect.OnNavigateToLeaderBoard)
                            }


                        if (state.value.roomData.rounds.lastOrNull()?.status == "success") {
                            updateState { copy(
                                gameMapMpState = GameMapState.Ready
                            ) }
                            if (room.players.any { !it.loadPanorama && it.online}) {
                                updateState { copy(
                                    gameMapMpState = GameMapState.WaitingPlayer
                                ) }
                            }else {
                                if (!state.value.isRoundStarted) {
                                    updateState { copy(isRoundStarted = true) }
                                    onIntent(GameMapMpIntent.OnStartTime)
                                }
                            }
                        }else {
                            updateState { copy(gameMapMpState = GameMapState.LoadingStreetView) }
                        }

                    }
                    result.isFailure -> {
                        onHandleErrorMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
                        sendEffect(GameMapMpEffect.OnBack)
                    }
                }
            }
        }
    }
    override suspend fun handleIntent(intent: GameMapMpIntent) {
        when (intent) {
            is GameMapMpIntent.UpdateUserLoadPanorama -> {
                updateState { copy(isSuccessLoadStreetView = intent.status) }
                updatePlayerData(hashMapOf(
                    "${state.value.userData.userId}/loadPanorama" to intent.status
                ), false)
            }

            is GameMapMpIntent.OnBackPressed -> {
                if (state.value.userData.userId == state.value.roomData.info.hostId) {
                    destroyRoom()
                }else {
                    updatePlayerData(
                        hashMapOf(
                            "${state.value.userData.userId}/ready" to false,
                            "${state.value.userData.userId}/online" to false,
                            "${state.value.userData.userId}/loadPanorama" to false
                        ), true
                    )
                }
            }

            is GameMapMpIntent.OnStartTime -> {
                startTimer()
            }

            is GameMapMpIntent.OnSaveCameraPosState -> {
                updateState { copy(cameraPositionState = intent.cameraState) }
            }
            is GameMapMpIntent.OnSaveLatLng -> {
                updateState { copy(latLng = intent.latLng) }
            }
            is GameMapMpIntent.OnSubmitAnswer -> {
                storeAnswer(intent.trueLoc)
            }

            is GameMapMpIntent.OnSubmitState -> {
                updateState { copy(isSubmit = true) }
            }
        }
    }

    private fun updatePlayerData(hashMap: HashMap<String, Any>, isBack: Boolean) {
        launchWithResult(
            showLoading = false,
            request = { updatePlayerUseCase(hashMap) },
            onSuccess = {
                if (isBack) sendEffect(GameMapMpEffect.OnBack)
            },
            onError = {
                sendEffect(GameMapMpEffect.ShowToast(it.message ?: "Something went wrong"))
                sendEffect(GameMapMpEffect.OnBack)
            }
        )
    }

    private fun storeAnswer(trueLoc: Pair<String, String>) {
        val guessedLoc = (state.value.latLng?.latitude ?: 0.0).toString() to
                (state.value.latLng?.longitude ?: 0.0).toString()
        val distance = countDistanceUseCase(trueLoc, guessedLoc)
        val answer = RoomAnswersDto(
            uid = state.value.userData.userId,
            lat = guessedLoc.first,
            lng = guessedLoc.second,
            distance = distance,
            point = calculatePointUseCase(distance)
        )
        launchWithResult(
            showLoading = false,
            request = { storeAnswerUseCase(answer, state.value.roomData.rounds.size, state.value.userData.userId) },
            onSuccess = {
                updateState { copy(isLoadingSubmit = false) }
                sendEffect(GameMapMpEffect.OnNavigateToResult)
            },
            onError = {
                updateState { copy(isLoadingSubmit = false) }
                sendEffect(GameMapMpEffect.ShowToast(it.message ?: "Something went wrong"))
            }
        )
    }


    override fun onShowLoading() {
        super.onShowLoading()
        updateState { copy(isLoading = true) }
    }

    override fun onHideLoading() {
        super.onHideLoading()
        updateState { copy(isLoading = false) }
    }

    override fun onHandleErrorMessage(message: String) {
        super.onHandleErrorMessage(message)
        sendEffect(GameMapMpEffect.OnBack)
    }

    fun startTimer() {
        updateState { copy(endTime =  System.currentTimeMillis()
                + (state.value.roomData.info.durationPerRound * 1000L)
        ) }

        viewModelScope.launch {
            while (System.currentTimeMillis() < state.value.endTime) {
                val remaining = (state.value.endTime - System.currentTimeMillis()) / 1000
                updateState { copy(timeLeft = remaining.toInt()) }
                Timber.d("remaining $remaining")
                delay(500)
            }
            updateState { copy(timeLeft = 0) }
            sendEffect(GameMapMpEffect.OnTimeUp)
        }
    }

    private fun destroyRoom() {
        updateState { copy(isLoadingBack = true) }
        launchWithResult(
            showLoading = false,
            request = { removeRoomUseCase() },
            onSuccess = {
                updateState { copy(isLoadingBack = false) }
                sendEffect(GameMapMpEffect.OnBack)
                        },
            onError = {
                updateState { copy(isLoadingBack = false) }
                sendEffect(GameMapMpEffect.ShowToast(it.message ?: "Something went wrong"))
            }
        )
    }
}