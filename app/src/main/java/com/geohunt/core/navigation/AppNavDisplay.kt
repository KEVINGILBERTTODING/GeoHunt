package com.geohunt.core.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.geohunt.core.vm.multiPlayer.MultiPlayerVm
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.room.ui.RoomScreen
import com.geohunt.presentation.home.ui.HomeScreen
import com.geohunt.presentation.leaderboard.ui.LeaderBoardScreen
import com.geohunt.presentation.loadingScreen.singlePlayer.ui.LoadingSinglePlayerScreen
import com.geohunt.presentation.map.mp.game.ui.GameMapMpScreen
import com.geohunt.presentation.map.mp.result.ui.GameResultMpScreen
import com.geohunt.presentation.map.singlePlayer.game.ui.GameMapSinglePlayerScreen
import com.geohunt.presentation.map.singlePlayer.result.ui.GameResultSingleScreen
import com.geohunt.presentation.splashScreen.ui.SplashScreen

@Composable
fun AppNavDisplay(modifier: Modifier) {
    val backStack = remember { mutableStateListOf<Route>(Route.SplashScreen) }
    val singlePlayerVm: SinglePlayerVm = hiltViewModel()
    val multiPlayerVm: MultiPlayerVm = hiltViewModel()
    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.SplashScreen> {
                SplashScreen(
                    onNavigateToHome = {
                        backStack.removeAll { it == Route.SplashScreen }
                        backStack.add(Route.HomeScreen)
                    }
                )
            }
            entry<Route.HomeScreen> {
                HomeScreen(
                    singlePlayerVm = singlePlayerVm,
                    multiPlayerVm = multiPlayerVm,
                    navigateToRoom = {
                        backStack.add(Route.RoomScreen)
                    },
                    navigateToLoadingScreen = {
                        backStack.add(Route.LoadingScreenSinglePlayer)
                    }
                )
            }
            entry(Route.LoadingScreenSinglePlayer) {
                LoadingSinglePlayerScreen(
                    singlePlayerVm = singlePlayerVm,
                    onBack = {
                        backStack.removeLastOrNull()
                    },
                    onNavigateToMap = {
                        backStack.removeAll { it == Route.LoadingScreenSinglePlayer }
                        backStack.add(Route.GameMapSinglePlayerScreen)
                    }
                )
            }
            entry<Route.GameMapSinglePlayerScreen> {
                GameMapSinglePlayerScreen(
                    singlePlayerVm = singlePlayerVm,
                    onBack = {
                        backStack.removeLastOrNull()
                    },
                    onNavigateToResult = {
                        backStack.removeAll { it == Route.GameMapSinglePlayerScreen }
                        backStack.add(Route.GameResultSinglePlayerScreen)
                    },
                    onNavigateToLoading = {
                        backStack.removeAll { it == Route.GameMapSinglePlayerScreen }
                        backStack.add(Route.LoadingScreenSinglePlayer)
                    }
                )
            }
            entry<Route.GameResultSinglePlayerScreen> {
                GameResultSingleScreen(
                    singlePlayerVm = singlePlayerVm,
                    onNavigateToLoading = {
                        backStack.add(Route.LoadingScreenSinglePlayer)
                    },
                    onNavigateToHome = {
                        backStack.removeAll { it == Route.GameResultSinglePlayerScreen }
                        backStack.add(Route.HomeScreen)
                    }
                )
            }
            entry<Route.RoomScreen> {
                RoomScreen(
                    multiPlayerVm = multiPlayerVm,
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    },
                    onNavigateToGame = {
                        backStack.removeAll { it == Route.RoomScreen }
                        backStack.add(Route.GameMapMpScreen)
                    },
                    onNavigateToLeaderBoard = {
                        backStack.removeAll { it != Route.HomeScreen }
                        backStack.add(Route.LeaderBoardScreen)
                    }
                )
            }
            entry<Route.GameMapMpScreen> {
                GameMapMpScreen(
                    multiPlayerVm = multiPlayerVm,
                    onNavigateToResult = {
                        backStack.removeAll { it == Route.GameMapMpScreen }
                        backStack.add(Route.GameResultMpScreen)
                    },
                    onBackToHome = {
                        backStack.removeAll { it != Route.HomeScreen }
                    },
                    onNavigateToLeaderBoard = {
                        backStack.removeAll { it != Route.HomeScreen }
                        backStack.add(Route.LeaderBoardScreen)
                    }
                )
            }
            entry<Route.GameResultMpScreen> {
                GameResultMpScreen(
                    mpVm = multiPlayerVm,
                    navigateToMap = {
                        backStack.removeAll { it == Route.GameResultMpScreen }
                        backStack.add(Route.GameMapMpScreen)
                    },
                    navigateToLeaderBoard = {
                        backStack.removeAll { it != Route.HomeScreen }
                        backStack.add(Route.LeaderBoardScreen)
                    },
                    navigateToHome = {
                        backStack.removeAll { it != Route.HomeScreen }
                    }
                )
            }

            entry<Route.LeaderBoardScreen>() {
                LeaderBoardScreen(
                    onNavigateToHome = {
                        backStack.removeAll { it != Route.HomeScreen }
                    }
                )
            }
        }
    )
}