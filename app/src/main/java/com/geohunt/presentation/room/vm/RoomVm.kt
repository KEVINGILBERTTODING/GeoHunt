package com.geohunt.presentation.room.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.data.dto.room.RoomPlayersDto
import com.geohunt.domain.usecase.DeleteRoomUseCase
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.MultiplayerValidationUseCase
import com.geohunt.domain.usecase.ObserveRoomDataUseCase
import com.geohunt.domain.usecase.UpdatePlayerUseCase
import com.geohunt.presentation.room.contract.RoomEffect
import com.geohunt.presentation.room.contract.RoomIntent
import com.geohunt.presentation.room.contract.RoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomVm @Inject constructor(
    private val observeRoomDataUseCase: ObserveRoomDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val multiplayerValidationUseCase: MultiplayerValidationUseCase,
    private val updatePlayerUseCase: UpdatePlayerUseCase
): BaseViewModel<RoomIntent, RoomUiState, RoomEffect>(
    initialState = RoomUiState()
) {
    var userData = getUserDataUseCase()

    init {
        viewModelScope.launch {
            observeRoomDataUseCase().collect { result ->
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
                        updateState {
                            copy(
                                isLoading = false,
                                error = result.exceptionOrNull()?.message ?: "Something went wrong"
                            )
                        }
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
                val players = hashMapOf<String, Any>()
                players["${userData.userId}/loadPanorama"] = false
                updatePlayer(players)
            }
        }
    }

    private fun updatePlayer(hashMap: HashMap<String, Any>) {
        launchWithResult(
            showLoading = false,
            request = { updatePlayerUseCase(hashMap) },
            onSuccess = {},
            onError = {}
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
    }

}