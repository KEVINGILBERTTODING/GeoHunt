package com.geohunt.presentation.room.vm

import androidx.lifecycle.SavedStateHandle
import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.MultiplayerValidationUseCase
import com.geohunt.domain.usecase.ObserveRoomDataUseCase
import com.geohunt.presentation.room.contract.RoomEffect
import com.geohunt.presentation.room.contract.RoomIntent
import com.geohunt.presentation.room.contract.RoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomVm @Inject constructor(
    private val observeRoomDataUseCase: ObserveRoomDataUseCase,
    savedStateHandle: SavedStateHandle,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val multiplayerValidationUseCase: MultiplayerValidationUseCase
): BaseViewModel<RoomIntent, RoomUiState, RoomEffect>(
    initialState = RoomUiState()
) {
    val roomId = checkNotNull(savedStateHandle.get<String>("id"))
    var userData = getUserDataUseCase()

    init {
        onIntent(RoomIntent.LoadRoomData)
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
        }
    }

    private fun loadRoom() {
        launchWithFlow(
            request = { observeRoomDataUseCase(roomId) },
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