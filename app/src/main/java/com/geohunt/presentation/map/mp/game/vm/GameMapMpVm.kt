package com.geohunt.presentation.map.mp.game.vm

import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.ObserveRoomDataUseCase
import com.geohunt.domain.usecase.UpdatePlayerUseCase
import com.geohunt.presentation.map.mp.game.contract.GameMapMpEffect
import com.geohunt.presentation.map.mp.game.contract.GameMapMpIntent
import com.geohunt.presentation.map.mp.game.contract.GameMapMpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameMapMpVm @Inject constructor(
    private val updatePlayerUseCase: UpdatePlayerUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val observeRoomDataUseCase: ObserveRoomDataUseCase
): BaseViewModel<GameMapMpIntent, GameMapMpUiState, GameMapMpEffect>(
    initialState = GameMapMpUiState()
) {
    val userData = getUserDataUseCase()

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
                                errorMsg = null,
                                roomData = room
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
    override suspend fun handleIntent(intent: GameMapMpIntent) {
        when (intent) {
            GameMapMpIntent.OnSubmit -> {}
            is GameMapMpIntent.UpdateUserLoadPanorama -> {
                updatePlayerData(hashMapOf(
                    "${userData.userId}/loadPanorama" to intent.status
                ))
            }

            is GameMapMpIntent.OnBackPressed -> {
                updatePlayerData(
                    hashMapOf(
                        "${userData.userId}/online" to false,
                        "${userData.userId}/loadPanorama" to false
                    )
                )
                sendEffect(GameMapMpEffect.OnBack)
            }
        }
    }

    private fun updatePlayerData(hashMap: HashMap<String, Any>) {
        launchWithResult(
            showLoading = false,
            request = { updatePlayerUseCase(hashMap) },
            onSuccess = {},
            onError = {
                sendEffect(GameMapMpEffect.ShowToast(it.message ?: "Something went wrong"))
                sendEffect(GameMapMpEffect.OnBack)
            }
        )
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
        viewModelScope.launch {
            handleIntent(GameMapMpIntent.OnBackPressed)
        }
    }
}