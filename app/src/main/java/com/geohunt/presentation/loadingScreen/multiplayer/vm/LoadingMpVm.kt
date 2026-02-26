package com.geohunt.presentation.loadingScreen.multiplayer.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.usecase.GetLoadingMessageUseCase
import com.geohunt.domain.usecase.GetTipsUseCase
import com.geohunt.presentation.loadingScreen.multiplayer.contract.LoadingMpEffect
import com.geohunt.presentation.loadingScreen.multiplayer.contract.LoadingMpIntent
import com.geohunt.presentation.loadingScreen.multiplayer.contract.LoadingMpUiState
import com.geohunt.presentation.loadingScreen.singlePlayer.event.LoadingSinglePlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingMpVm @Inject constructor(
    private val getLoadingMessageUseCase: GetLoadingMessageUseCase,
    private val getTipsUseCase: GetTipsUseCase
): BaseViewModel<LoadingMpIntent, LoadingMpUiState, LoadingMpEffect>(
    initialState = LoadingMpUiState()
) {
    init {
        getLoadingMsg()
        getTipsMsg()
    }

    private fun getLoadingMsg() {
        val loadingMsg = getLoadingMessageUseCase()
        viewModelScope.launch {
            loadingMsg.forEach {
                updateState { copy(loadingMsg = it) }
                delay(1000)
            }
        }
    }
    private fun getTipsMsg() {
        val tipsMsg = getTipsUseCase()
        viewModelScope.launch {
            tipsMsg.forEach {
                updateState { copy(tipsMsg = it) }
                delay(5000)
            }
        }
    }

    override suspend fun handleIntent(intent: LoadingMpIntent) {
    }
}