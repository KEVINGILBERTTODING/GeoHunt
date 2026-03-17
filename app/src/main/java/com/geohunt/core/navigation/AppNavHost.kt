package com.geohunt.core.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.geohunt.core.vm.multiPlayer.MultiPlayerVm
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.room.ui.RoomScreen
import com.geohunt.presentation.home.ui.HomeScreen
import com.geohunt.presentation.loadingScreen.singlePlayer.ui.LoadingSinglePlayerScreen
import com.geohunt.presentation.map.mp.game.ui.GameMapMpScreen
import com.geohunt.presentation.map.mp.result.ui.GameResultMpScreen
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
                val singlePlayerVm: SinglePlayerVm = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
                val multiPlayerVm: MultiPlayerVm = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
                HomeScreen(singlePlayerVm, multiPlayerVm, { id ->
                    navController.navigate(Screen.MultiplayerGraph.route)
                }, {
                    navController.navigate(Screen.LoadingScreenSinglePlayer.route)
                })
            }
            composable(Screen.LoadingScreenSinglePlayer.route) {
                val singlePlayerVm: SinglePlayerVm = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
                LoadingSinglePlayerScreen(singlePlayerVm, {
                    navController.popBackStack()
                }, {
                    navController.navigate(Screen.GameMapSinglePlayerScreen.route) {
                        popUpTo(Screen.LoadingScreenSinglePlayer.route){
                            inclusive = true
                        }
                    }
                })
            }
            composable(Screen.GameMapSinglePlayerScreen.route) {
                val singlePlayerVm: SinglePlayerVm = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
                GameMapSinglePlayerScreen(
                    singlePlayerVm,
                    {
                        navController.navigate(Screen.GameResultSinglePlayerScreen.route) {
                            popUpTo(Screen.GameMapSinglePlayerScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    {
                        navController.popBackStack()
                    },
                    {
                        navController.navigate(Screen.LoadingScreenSinglePlayer.route) {
                            popUpTo(Screen.GameMapSinglePlayerScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.GameResultSinglePlayerScreen.route) {
                val singlePlayerVm: SinglePlayerVm = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)

                GameResultSingleScreen(
                    singlePlayerVm,
                    {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.GameResultSinglePlayerScreen.route) {
                                inclusive = true
                            }
                        }
                    }, {
                        navController.navigate(Screen.LoadingScreenSinglePlayer.route)
                    })
            }
        }

        // multiplayer
        navigation(
            route = Screen.MultiplayerGraph.route,
            startDestination = Screen.RoomScreen.route
        ) {
            composable(Screen.RoomScreen.route) { backStackEntry ->
                val multiPlayerVm: MultiPlayerVm = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
                RoomScreen({
                    navController.popBackStack()
                }, multiPlayerVm, {
                    navController.navigate(Screen.GameMapMpScreen.route) {
                        popUpTo(Screen.RoomScreen.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(Screen.GameMapMpScreen.route) { backStackEntry ->
                val multiPlayerVm: MultiPlayerVm = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
                GameMapMpScreen(multiPlayerVm, {
                    navController.popBackStack()
                }, {
                    navController.navigate(Screen.GameResultMpScreen.route) {
                        popUpTo(Screen.GameMapMpScreen.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(Screen.GameResultMpScreen.route) { backStackEntry ->
                val multiPlayerVm: MultiPlayerVm = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
                GameResultMpScreen({
                    navController.popBackStack()
                }, {
                    navController.navigate(Screen.GameMapMpScreen.route){
                        popUpTo(Screen.GameResultMpScreen.route) {
                            inclusive = true
                        }
                    }
                }, multiPlayerVm)
            }
        }

    }
}