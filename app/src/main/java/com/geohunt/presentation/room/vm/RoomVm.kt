package com.geohunt.presentation.room.vm

import androidx.lifecycle.SavedStateHandle
import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.usecase.ObserveRoomDataUseCase
import com.geohunt.presentation.room.contract.RoomEffect
import com.geohunt.presentation.room.contract.RoomIntent
import com.geohunt.presentation.room.contract.RoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomVm @Inject constructor(
    private val observeRoomDataUseCase: ObserveRoomDataUseCase,
    savedStateHandle: SavedStateHandle
): BaseViewModel<RoomIntent, RoomUiState, RoomEffect>(
    initialState = RoomUiState()
) {
    val roomId = checkNotNull(savedStateHandle.get<String>("id"))

    init {
        onIntent(RoomIntent.LoadRoomData)
    }
    override suspend fun handleIntent(intent: RoomIntent) {
        when(intent) {
            RoomIntent.LoadRoomData -> {
                loadRoom()
            }
        }
    }

    private fun loadRoom() {
        launchWithFlow(
            request = { observeRoomDataUseCase(roomId) },
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