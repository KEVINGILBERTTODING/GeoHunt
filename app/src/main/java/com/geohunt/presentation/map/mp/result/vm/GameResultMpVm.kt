package com.geohunt.presentation.map.mp.result.vm

import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.usecase.CheckMinimumPlayerUseCase
import com.geohunt.domain.usecase.DeleteRoomUseCase
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.MultiplayerValidationUseCase
import com.geohunt.domain.usecase.ObserveRoomDataUseCase
import com.geohunt.domain.usecase.UpdatePlayerUseCase
import com.geohunt.presentation.map.mp.result.contract.GameResultMpEffect
import com.geohunt.presentation.map.mp.result.contract.GameResultMpIntent
import com.geohunt.presentation.map.mp.result.contract.GameResultMpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameResultMpVm @Inject constructor(
    private val observeRoomDataUseCase: ObserveRoomDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val multiplayerValidationUseCase: MultiplayerValidationUseCase,
    private val updatePlayerUseCase: UpdatePlayerUseCase,
    private val removeRoomUseCase: DeleteRoomUseCase,
    private val checkMinimumPlayerUseCase: CheckMinimumPlayerUseCase
):
    BaseViewModel<GameResultMpIntent, GameResultMpUiState, GameResultMpEffect>(
        initialState = GameResultMpUiState()
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
                                    error = null,
                                    room = room,
                                    isHost = userData.userId == room.info.hostId
                                )
                            }

                            state.value.room.rounds.lastOrNull()?.let {
                                if (it.status == "loading") {
                                    checkMinimumPlayerUseCase.invoke(room)
                                        .onFailure {
                                            sendEffect(GameResultMpEffect.ShowToast("Not enough players, stopping..."))
                                            sendEffect(GameResultMpEffect.OnCancelGame)
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
        }

    override suspend fun handleIntent(intent: GameResultMpIntent) {
        when(intent) {
            GameResultMpIntent.OnBack -> {
            }
            GameResultMpIntent.OnStartGame -> {

            }
        }
    }
}