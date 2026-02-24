package com.geohunt.presentation.home.vm

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.core.resource.Resource
import com.geohunt.domain.usecase.CreateRoomUseCase
import com.geohunt.presentation.home.event.RoomFormEvent
import com.geohunt.presentation.home.state.RoomFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomVm @Inject constructor(
    private val createRoomUseCase: CreateRoomUseCase
): ViewModel() {
    val formState = MutableStateFlow(RoomFormState().copy(
        totalRounds = "5", durationPerRound = "120"
    ))
    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val event = MutableSharedFlow<RoomFormEvent>()

    fun onRoomRoundChange(value: String) {
        formState.update {
            it.copy(
                totalRounds = value,
                totalRoundsError = validateRound(value)
            )
        }
    }

    fun validateRound(value: String): String? {
        val round = value.toIntOrNull()
        return when {
            round == null -> "Invalid round"
            round < 1 -> "Round must be greater than 0"
            round > 10 -> "Round must be less than 10"
            else -> null
        }
    }

    fun onRoomDurationChange(value: String) {
        formState.update {
            it.copy(
                durationPerRound = value,
                durationPerRoundError = validateDurationRound(value)
            )
        }
    }

    fun validateDurationRound(value: String): String? {
        val round = value.toIntOrNull()
        return when {
            round == null -> "Invalid duration"
            round < 30 -> "Duration must be greater than 30 seconds"
            round > 300 -> "Duration must be less than 300 seconds"
            else -> null
        }
    }

    fun submit() {
        viewModelScope.launch {
            uiState.value = Resource.Loading
            val totalRoundError = formState.value.totalRoundsError
            val durationRoundError = formState.value.durationPerRoundError

            if (totalRoundError != null) {
                event.emit(RoomFormEvent.ShowToastEvent(totalRoundError))
                uiState.value = Resource.Idle
                return@launch
            }
            if (durationRoundError != null) {
                event.emit(RoomFormEvent.ShowToastEvent(durationRoundError))
                uiState.value = Resource.Idle
                return@launch
            }

            val createRoom = createRoomUseCase(formState.value.totalRounds.toInt(),
                formState.value.durationPerRound.toInt())

            if (createRoom.isSuccess) {
                uiState.value = Resource.Idle
                event.emit(RoomFormEvent.NavigateToRoom)
            }else {
                uiState.value = Resource.Idle
                event.emit(RoomFormEvent.ShowToastEvent(
                    createRoom.exceptionOrNull()?.message ?: "Something went wrong"))
            }

        }
    }

    fun setEvent(formEvent: RoomFormEvent) {
        viewModelScope.launch { event.emit(formEvent) }
    }
}