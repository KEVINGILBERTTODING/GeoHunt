package com.geohunt.presentation.home.event

sealed class HomeEvent {
    object StartGame : HomeEvent()
    object ShowCountryBottomSheet: HomeEvent()
    object BackPressed: HomeEvent()
}