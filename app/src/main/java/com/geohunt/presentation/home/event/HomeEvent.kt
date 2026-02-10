package com.geohunt.presentation.home.event

sealed class HomeEvent {
    object startGame : HomeEvent()
    object showCityBottomSheet: HomeEvent()
}