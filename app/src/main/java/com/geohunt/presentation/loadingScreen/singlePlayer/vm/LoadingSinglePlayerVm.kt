package com.geohunt.presentation.loadingScreen.singlePlayer.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.domain.usecase.GetLoadingMessageUseCase
import com.geohunt.domain.usecase.GetTipsUseCase
import com.geohunt.presentation.loadingScreen.singlePlayer.event.LoadingSinglePlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingSinglePlayerVm @Inject constructor(
    private val getLoadingMessageUseCase: GetLoadingMessageUseCase,
    private val getTipsUseCase: GetTipsUseCase
): ViewModel() {
    private val _loadingMsg = MutableStateFlow(getLoadingMessageUseCase().first())
    val loadingMsg = _loadingMsg.asStateFlow()

    private val _loadingSinglePlayerEvent = MutableSharedFlow<LoadingSinglePlayerEvent>()
    val loadingSinglePlayerEvent = _loadingSinglePlayerEvent

    private val _tipsMessage = MutableStateFlow(getTipsUseCase().random())
    val tipsMessage = _tipsMessage.asStateFlow()

    init {
        getLoadingMsg()
        getTipsMsg()
    }

    fun navigateToMap() {
        viewModelScope.launch {
            _loadingSinglePlayerEvent.emit(LoadingSinglePlayerEvent.navigateToMap)
        }
    }

    private fun getLoadingMsg() {
        val loadingMsg = getLoadingMessageUseCase()
        viewModelScope.launch {
            loadingMsg.forEach {
                _loadingMsg.value = it
                delay(1000)
            }
        }
    }

    fun navigateToHome() {
        viewModelScope.launch {
            loadingSinglePlayerEvent.emit(LoadingSinglePlayerEvent.NavigateToHome)
        }
    }

    private fun getTipsMsg() {
        val tipsMsg = getTipsUseCase()
        viewModelScope.launch {
            tipsMsg.forEach {
                _tipsMessage.value = it
                delay(5000)
            }
        }
    }
}