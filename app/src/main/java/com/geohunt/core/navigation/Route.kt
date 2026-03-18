package com.geohunt.core.navigation

sealed interface Route {
    data object SplashScreen : Route
    data object HomeScreen : Route
    data object LoadingScreenSinglePlayer : Route
    data object GameMapSinglePlayerScreen : Route
    data object GameResultSinglePlayerScreen : Route
    data object RoomScreen : Route
    data object GameMapMpScreen : Route
    data object GameResultMpScreen : Route
}