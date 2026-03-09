package com.geohunt.presentation.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geohunt.core.base.BaseViewModel
import com.geohunt.core.resource.Resource
import com.geohunt.domain.usecase.JoinRoomUseCase
import com.geohunt.presentation.home.contract.JoinRoomEffect
import com.geohunt.presentation.home.contract.JoinRoomIntent
import com.geohunt.presentation.home.contract.JoinRoomUiState
import com.geohunt.presentation.home.event.JoinRoomEvent
import com.geohunt.presentation.home.state.JoinRoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class JoinRoomVm @Inject constructor(
    private val joinRoomUseCase: JoinRoomUseCase
): BaseViewModel<JoinRoomIntent, JoinRoomUiState, JoinRoomEffect>(
    initialState = JoinRoomUiState()
) {

    fun validationRoomCode(roomCode: String?): String? {
        return when {
            roomCode.isNullOrEmpty() -> "Room code cannot be empty"
            roomCode.length != 6 -> "Room code must be 6 digits"
            else -> null
        }
    }

    fun submit() {
        Timber.d("room code ${state.value.roomCode}")
        val roomCodeError = validationRoomCode(state.value.roomCode)
        if (roomCodeError != null){
            sendEffect(JoinRoomEffect.ShowToast(roomCodeError))
            updateState { copy(errorMsg = roomCodeError) }
            return
        }
        launchWithResult(
            request = { joinRoomUseCase(state.value.roomCode) },
            onSuccess = {
                sendEffect(JoinRoomEffect.NavigateToRoom(state.value.roomCode))
                updateState { copy(roomCode = "", errorMsg = null) }
            }
        )

    }

    override suspend fun handleIntent(intent: JoinRoomIntent) {
        when(intent) {
            is JoinRoomIntent.OnRoomCodeChanged -> {
                updateState { copy(roomCode = intent.value) }
            }
            is JoinRoomIntent.OnSubmit -> {
                submit()
            }
        }
    }

    override fun onShowLoading() {
        super.onShowLoading()
        updateState { copy(isLoading = true, errorMsg = null) }
    }

    override fun onHideLoading() {
        super.onHideLoading()
        updateState { copy(isLoading = false) }
    }

    override fun onHandleErrorMessage(message: String) {
        super.onHandleErrorMessage(message)
        updateState { copy(errorMsg = message) }
        sendEffect(JoinRoomEffect.ShowToast(message))
    }
}