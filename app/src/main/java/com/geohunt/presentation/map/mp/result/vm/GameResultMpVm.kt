package com.geohunt.presentation.map.mp.result.vm

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.model.RoundResult
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Round
import com.geohunt.domain.repository.ColorRepository
import com.geohunt.domain.usecase.CalculateLeaderBoard
import com.geohunt.domain.usecase.CheckMinimumPlayerUseCase
import com.geohunt.domain.usecase.DeleteRoomUseCase
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.MultiplayerValidationUseCase
import com.geohunt.domain.usecase.ObserveRoomDataUseCase
import com.geohunt.domain.usecase.UpdatePlayerUseCase
import com.geohunt.presentation.map.mp.result.contract.GameResultMpEffect
import com.geohunt.presentation.map.mp.result.contract.GameResultMpIntent
import com.geohunt.presentation.map.mp.result.contract.GameResultMpUiState
import com.geohunt.presentation.map.mp.result.contract.GameResultState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ln

@HiltViewModel
class GameResultMpVm @Inject constructor(
    private val observeRoomDataUseCase: ObserveRoomDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updatePlayerUseCase: UpdatePlayerUseCase,
    private val removeRoomUseCase: DeleteRoomUseCase,
    private val checkMinimumPlayerUseCase: CheckMinimumPlayerUseCase,
    private val calculateLeaderBoard: CalculateLeaderBoard,
    private val colorRepository: ColorRepository
):
    BaseViewModel<GameResultMpIntent, GameResultMpUiState, GameResultMpEffect>(
        initialState = GameResultMpUiState()
    ) {
        init {
            updateState { copy(userData = getUserDataUseCase()) }
            viewModelScope.launch {
                onShowLoading()
                // reset load panorama
                updatePlayerData(
                    hashMapOf(
                        "${state.value.userData.userId}/loadPanorama" to false
                    ), true
                )
                observeRoomDataUseCase().collect { result ->
                    onHideLoading()
                    when {
                        result.isSuccess -> {
                            val room = result.getOrThrow()
                            if (state.value.currentRoundIndex < 0) {
                                updateState { copy(currentRoundIndex = room.rounds.size - 1) }
                            }
                            updateState {
                                copy(
                                    isLoading = false,
                                    error = null,
                                    isHost = userData.userId == room.info.hostId,
                                    leaderBoardList = calculateLeaderBoard(room),
                                    point = room.rounds.getOrNull(state.value.currentRoundIndex)?.answers?.find {
                                        it.uid == state.value.userData.userId }?.point ?: 0,
                                    answerList = room.rounds.getOrNull(state.value.currentRoundIndex)?.answers?.sortedByDescending { it.point }?.map { it } ?: emptyList(),
                                    roundResultList = room.rounds.getOrNull(state.value.currentRoundIndex)?.answers
                                        ?.sortedByDescending { it.point }
                                        ?.map { answers ->
                                            RoundResult(
                                                player = room.players.find { it.uid == answers.uid } ?: Player(),
                                                lat = answers.lat,
                                                lng = answers.lng,
                                                point = answers.point,
                                                distance = answers.distance
                                            )
                                        } ?: emptyList(),
                                    round = room.rounds.getOrNull(state.value.currentRoundIndex)?: Round(),
                                    trueLocColor = colorRepository.getTrueLocationColor(),
                                    isFinished = room.info.totalRounds == state.value.currentRoundIndex + 1,
                                )
                            }

                            // observe next round
                            if (!state.value.isFinished) {
                                room.rounds.getOrNull(state.value.currentRoundIndex + 1)?.let {
                                    when(it.status) {
                                        "loading" -> {
                                            updateState { copy(gameResultState = GameResultState.Loading) }
                                            checkMinimumPlayerUseCase.invoke(room)
                                                .onFailure {
                                                    updateState { copy(gameResultState = GameResultState.Idle) }
                                                    sendEffect(GameResultMpEffect.ShowToast("Not enough players, stopping..."))
                                                    sendEffect(GameResultMpEffect.OnNavigateToLeaderBoard)
                                                }
                                        }
                                        "success" -> {
                                            sendEffect(GameResultMpEffect.OnNavigateToMap)
                                        }
                                        "error" -> {
                                            updateState { copy(gameResultState = GameResultState.Error) }
                                        }
                                    }
                                }
                            }
                        }
                        result.isFailure -> {
                            onHandleErrorMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
                        }
                    }
                }
            }

            startTimer()
        }

    override suspend fun handleIntent(intent: GameResultMpIntent) {
        when(intent) {
            GameResultMpIntent.OnBackPressed -> {
                if (state.value.isHost) {
                    destroyRoom()
                }else {
                    updatePlayerData(
                        hashMapOf(
                            "${state.value.userData.userId}/ready" to false,
                            "${state.value.userData.userId}/online" to false,
                            "${state.value.userData.userId}/loadPanorama" to false
                        ),
                        false
                    )
                }
            }

            is GameResultMpIntent.OnSetCameraState -> {
                setCameraState(intent.lat, intent.lng)
            }
        }
    }

    private fun setCameraState(lat: String = "0.0", lng: String = "0.0") {
        viewModelScope.launch {
            state.value.cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(lat.toDoubleOrNull() ?: 0.0,
                        lng.toDoubleOrNull() ?: 0.0),
                    15f
                )
            )
        }
    }

    private fun startTimer() {
       updateState {
           copy(
               endTime = System.currentTimeMillis()
                       + (if (!state.value.isFinished) 15 else 10) * 1000L
           )
       }

        viewModelScope.launch {
            while (System.currentTimeMillis() < state.value.endTime) {
                val remaining = (state.value.endTime - System.currentTimeMillis()) / 1000
                updateState { copy(timeLeft = remaining.toInt()) }
                Timber.d("remaining $remaining")
                delay(500)
            }
            updateState { copy(timeLeft = 0) }

            if (state.value.isFinished) {
                sendEffect(GameResultMpEffect.OnNavigateToLeaderBoard)
            }else {
                if (state.value.isHost) {
                    sendEffect(GameResultMpEffect.OnStartGame)
                }
            }
        }
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
        sendEffect(GameResultMpEffect.ShowToast(message))
        sendEffect(GameResultMpEffect.OnBack)
    }

    private fun updatePlayerData(hashMap: HashMap<String, Any>, isUpdatePanorama: Boolean) {
        launchWithResult(
            showLoading = false,
            request = { updatePlayerUseCase(hashMap) },
            onSuccess = {
                if (!isUpdatePanorama) sendEffect(GameResultMpEffect.OnBack)
            },
            onError = {
                if (!isUpdatePanorama) {
                    sendEffect(GameResultMpEffect.ShowToast(it.message ?: "Something went wrong"))
                    sendEffect(GameResultMpEffect.OnBack)
                }
            }
        )
    }
    private fun destroyRoom() {
        updateState { copy(isLoadingBack = true) }
        launchWithResult(
            showLoading = false,
            request = { removeRoomUseCase() },
            onSuccess = {
                updateState { copy(isLoadingBack = false) }
                sendEffect(GameResultMpEffect.OnBack)
            },
            onError = {
                updateState { copy(isLoadingBack = false) }
                sendEffect(GameResultMpEffect.ShowToast(it.message ?: "Something went wrong"))
            }
        )
    }

}