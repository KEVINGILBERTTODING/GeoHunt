package com.geohunt.presentation.room.vm

import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.usecase.DeleteRoomUseCase
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.MultiplayerValidationUseCase
import com.geohunt.domain.usecase.ObserveRoomDataUseCase
import com.geohunt.domain.usecase.UpdatePlayerUseCase
import com.geohunt.presentation.map.mp.game.contract.GameMapMpEffect
import com.geohunt.presentation.room.contract.RoomEffect
import com.geohunt.presentation.room.contract.RoomIntent
import com.geohunt.presentation.room.contract.RoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomVm @Inject constructor(
    private val observeRoomDataUseCase: ObserveRoomDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val multiplayerValidationUseCase: MultiplayerValidationUseCase,
    private val updatePlayerUseCase: UpdatePlayerUseCase,
    private val removeRoomUseCase: DeleteRoomUseCase
): BaseViewModel<RoomIntent, RoomUiState, RoomEffect>(
    initialState = RoomUiState()
) {
    var userData = getUserDataUseCase()

    init {
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
                                room = room
                            )
                        }
                    }
                    result.isFailure -> {
                        onHandleErrorMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
                    }
                }
            }
        }
    }

    override suspend fun handleIntent(intent: RoomIntent) {
        when(intent) {
            is RoomIntent.LoadRoomData -> {
                loadRoom()
            }
            is RoomIntent.OnStartGame -> {
                sendEffect(RoomEffect.StartGame)
//                val validationResult = multiplayerValidationUseCase(state.value.room)
//                validationResult
//                    .onSuccess {
//                        sendEffect(RoomEffect.StartGame)
//                    }
//                    .onFailure {
//                        sendEffect(RoomEffect.ShowToast(it.message ?: "Something went wrong"))
//                    }
            }

            is RoomIntent.OnPlayerReady -> {
                Timber.d("onready ${intent.isReady}")
                val players = hashMapOf<String, Any>()
                players["${userData.userId}/ready"] = intent.isReady
                updatePlayer(players, false)
            }

            is RoomIntent.OnBack -> {
                if (userData.userId == state.value.room.info.hostId) {
                    removeRoom()
                }else {
                    updatePlayer(hashMapOf("${userData.userId}/ready" to false), true)
                }
            }
        }
    }

    private fun updatePlayer(hashMap: HashMap<String, Any>, isBack: Boolean) {
        launchWithResult(
            showLoading = false,
            request = { updatePlayerUseCase(hashMap) },
            onSuccess = {
                if (isBack) sendEffect(RoomEffect.OnBack)
            },
            onError = {
                sendEffect(RoomEffect.ShowToast(it.message ?: "Something went wrong"))
            }
        )
    }


    private fun loadRoom() {
        launchWithFlow(
            request = { observeRoomDataUseCase() },
            onError = {
                onHandleErrorMessage(it.message ?: "Something went wrong")
                sendEffect(RoomEffect.OnBack)
            },
            onSuccess = { roomData ->
                updateState { copy(isLoading = false, error = null, room = roomData) }
            }
        )
    }

    override fun onShowLoading() {
        super.onShowLoading()
        updateState {
            copy(isLoading = true, error = null)
        }
    }

    override fun onHideLoading() {
        super.onHideLoading()
        updateState {
            copy(isLoading = false)
        }
    }

    override fun onHandleErrorMessage(message: String) {
        super.onHandleErrorMessage(message)
        updateState { copy(error = message) }
        sendEffect(RoomEffect.ShowToast(message))
        sendEffect(RoomEffect.OnBack)
    }

    private fun removeRoom() {
        updateState { copy(isLoadingBack = true) }
        launchWithResult(
            showLoading = false,
            request = { removeRoomUseCase() },
            onSuccess = {
                updateState { copy(isLoadingBack = false) }
            },
            onError = {
                updateState { copy(isLoadingBack = false) }
                sendEffect(RoomEffect.ShowToast(it.message ?: "Something went wrong"))
            }
        )
    }

}