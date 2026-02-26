package com.geohunt.presentation.home.vm

import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.usecase.CreateRoomUseCase
import com.geohunt.presentation.home.contract.CreateRoomEffect
import com.geohunt.presentation.home.contract.CreateRoomIntent
import com.geohunt.presentation.home.contract.CreateRoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomVm @Inject constructor(
    private val createRoomUseCase: CreateRoomUseCase
): BaseViewModel<CreateRoomIntent, CreateRoomUiState, CreateRoomEffect>(
    initialState = CreateRoomUiState(durationPerRound = "300", totalRounds = "5")
) {

    override suspend fun handleIntent(intent: CreateRoomIntent) {
        when(intent) {
            is CreateRoomIntent.OnSubmit -> {
                submit()
            }

            is CreateRoomIntent.OnDurationRoundChanged -> {
                updateState { copy(durationPerRound = intent.value) }
            }
            is CreateRoomIntent.OnTotalRoundChanged -> {
                updateState { copy(totalRounds = intent.value) }
            }

            is CreateRoomIntent.OnSaveCountryId -> {
                updateState { copy(countryId = intent.value) }
            }
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


    fun validateDurationRound(value: String): String? {
        val round = value.toIntOrNull()
        return when {
            round == null -> "Invalid duration"
            round < 30 -> "Duration must be greater than 30 seconds"
            round > 300 -> "Duration must be less than 300 seconds"
            else -> null
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
        sendEffect(CreateRoomEffect.ShowToast(message))
    }

    fun submit() {
        viewModelScope.launch {
            val totalRoundError = validateRound(state.value.totalRounds)
            val durationRoundError = validateDurationRound(state.value.durationPerRound)

            if (totalRoundError != null) {
                sendEffect(CreateRoomEffect.ShowToast(totalRoundError))
                updateState { copy(isLoading = false, errorMsg = totalRoundError) }
                return@launch
            }
            if (durationRoundError != null) {
                sendEffect(CreateRoomEffect.ShowToast(durationRoundError))
                updateState { copy(isLoading = false, errorMsg = durationRoundError) }
                return@launch
            }

            launchWithResult(
                request = {
                    createRoomUseCase(state.value.totalRounds.toInt(),
                        state.value.durationPerRound.toInt(),
                        state.value.countryId)
                },
                onSuccess = { roomId ->
                    sendEffect(CreateRoomEffect.NavigateToRoom(roomId))
                }
            )

        }
    }

}