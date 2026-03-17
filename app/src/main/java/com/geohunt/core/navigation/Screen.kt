package com.geohunt.core.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object HomeScreen : Screen("home")
    object LoadingScreenSinglePlayer : Screen("loading_single_player")
    object HomeGraph : Screen("home_graph")
    object GameMapSinglePlayerScreen : Screen("game_map_single_player")
    object GameResultSinglePlayerScreen : Screen("game_result_single_player")
    object RoomScreen : Screen("room_screen")
    object GameMapMpScreen : Screen("game_map_mp_screen")
    object GameResultMpScreen : Screen("game_result_mp_screen")
    object MultiplayerGraph : Screen("multiplayer_graph")

}