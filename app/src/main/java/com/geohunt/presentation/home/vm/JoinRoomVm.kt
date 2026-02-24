package com.geohunt.presentation.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geohunt.core.resource.Resource
import com.geohunt.domain.usecase.JoinRoomUseCase
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
): ViewModel() {
    val formState = MutableStateFlow(JoinRoomState(roomCode = ""))
    val event = MutableSharedFlow<JoinRoomEvent>()
    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Idle)

    fun setEvent(eventVal: JoinRoomEvent) {
       viewModelScope.launch {
           event.emit(eventVal)
       }
    }

    fun onRoomCodeChanged(roomCode: String) {
        formState.update {
            it.copy(roomCode = roomCode)
        }
    }

    fun validationRoomCode(roomCode: String?): String? {
        return when {
            roomCode.isNullOrEmpty() -> "Room code cannot be empty"
            roomCode.length != 6 -> "Room code must be 6 digits"
            else -> null
        }
    }

    fun submit() {
        val roomCodeError = validationRoomCode(formState.value.roomCode)
        if (roomCodeError != null){
            setEvent(JoinRoomEvent.ShowToast(roomCodeError))
            uiState.value = Resource.Idle
            return
        }

        uiState.value = Resource.Loading

        viewModelScope.launch {
            val joinRoomResponse = joinRoomUseCase(formState.value.roomCode)
            if (joinRoomResponse.isSuccess) {
                setEvent(JoinRoomEvent.NavigateToRoom)
                uiState.value = Resource.Idle
            }else {
                setEvent(JoinRoomEvent.ShowToast(joinRoomResponse.exceptionOrNull()?.message ?: "Something went wrong"))
                uiState.value = Resource.Idle
            }
        }

    }

}