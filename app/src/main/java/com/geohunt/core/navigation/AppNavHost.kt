package com.geohunt.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.room.ui.RoomScreen
import com.geohunt.presentation.home.ui.HomeScreen
import com.geohunt.presentation.loadingScreen.singlePlayer.ui.LoadingSinglePlayerScreen
import com.geohunt.presentation.map.singlePlayer.game.ui.GameMapSinglePlayerScreen
import com.geohunt.presentation.map.singlePlayer.result.ui.GameResultSingleScreen
import com.geohunt.presentation.splashScreen.ui.SplashScreen

@Composable
fun AppNavhost(navController: NavHostController = rememberNavController(), modifier: Modifier) {
    NavHost(navController = navController, startDestination = Screen.HomeGraph.route) {
        navigation(
            route = Screen.HomeGraph.route,
            startDestination = Screen.SplashScreen.route
        ){
            composable(Screen.SplashScreen.route) {
                SplashScreen(navController)
            }
            composable(Screen.HomeScreen.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.HomeGraph.route)
                }
                val sharedVm = hiltViewModel<SinglePlayerVm>(parentEntry)
                HomeScreen(sharedVm, { id ->
                    navController.navigate(Screen.RoomScreen.createRoute(id))
                }, {
                    navController.navigate(Screen.LoadingScreenSinglePlayer.route)
                })
            }
            composable(Screen.LoadingScreenSinglePlayer.route) {
                LoadingSinglePlayerScreen(navController)
            }
            composable(Screen.GameMapSinglePlayerScreen.route) {
                GameMapSinglePlayerScreen(navController)
            }
            composable(Screen.GameResultSinglePlayerScreen.route) {
                GameResultSingleScreen(navController)
            }
            composable(Screen.RoomScreen.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )) {
                RoomScreen({
                    navController.popBackStack()
                }, {
                })
            }
        }

    }
}