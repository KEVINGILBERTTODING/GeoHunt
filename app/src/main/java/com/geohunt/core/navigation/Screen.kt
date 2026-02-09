package com.geohunt.core.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object HomeScreen : Screen("home")
    object LoadingScreenSinglePlayer : Screen("loading_single_player")
    object HomeGraph : Screen("home_graph")
    object GameMapSinglePlayerScreen : Screen("game_map_single_player")
}