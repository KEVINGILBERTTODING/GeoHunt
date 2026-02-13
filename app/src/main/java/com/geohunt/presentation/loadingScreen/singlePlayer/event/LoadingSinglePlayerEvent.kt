package com.geohunt.presentation.loadingScreen.singlePlayer.event

sealed class LoadingSinglePlayerEvent{
    object navigateToMap: LoadingSinglePlayerEvent()
    object NavigateToHome: LoadingSinglePlayerEvent()
}