package com.geohunt.presentation.loadingScreen.multiplayer.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState

sealed class LoadingMpIntent: MviIntent {

}

sealed class LoadingMpEffect: MviEffect {

}

data class LoadingMpUiState(
    val tipsMsg: String = "",
    val loadingMsg: String = ""
): MviState
