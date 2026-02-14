package com.geohunt.presentation.home.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.geohunt.R
import com.geohunt.core.navigation.Screen
import com.geohunt.core.resource.Resource
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.home.component.CountryBottomSheet
import com.geohunt.presentation.home.event.HomeEvent
import com.geohunt.presentation.home.vm.HomeVm
import timber.log.Timber
import kotlin.math.sin

@SuppressLint("ContextCastToActivity", "UnrememberedGetBackStackEntry",
    "ConfigurationScreenWidthHeight"
)
@Composable
fun HomeScreen(navController: NavController = rememberNavController()) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.location_marker)
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val parentEntry = navController.getBackStackEntry(Screen.HomeGraph.route)
    val singlePlayerVm: SinglePlayerVm = hiltViewModel(parentEntry)
    val homeVm: HomeVm = hiltViewModel()
    val usernameState by homeVm.userNameState.collectAsStateWithLifecycle()
    val homeState by homeVm.homeState.collectAsStateWithLifecycle()
    val countryState by homeVm.countryState.collectAsStateWithLifecycle()
    var showDialogBackPressed by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    val buttonColor = when(homeState) {
        is Resource.Success -> Green41B
        else -> Color.Gray
    }
    val buttonText = when(homeState) {
        is Resource.Loading -> stringResource(R.string.loading_game)
        else -> stringResource(R.string.start)
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (showBottomSheet) {
        CountryBottomSheet(
            homeVm = homeVm,
            onClick = { country ->
                homeVm.setSelectedCountry(country)
            },
            onDissmiss = { showBottomSheet = false }
        )
    }

    if (showDialogBackPressed) {
        ConfirmationBottomSheet(stringResource(R.string.confirm_exit),
            stringResource(R.string.are_you_sure_you_want_to_exit_the_game),
            stringResource(R.string.keep_playing),
            stringResource(R.string.yes_exit),
            {
                showDialogBackPressed = false
            },
            {
                showDialogBackPressed = false
            }) {
            activity?.finish()
        }
    }

    LaunchedEffect(homeState) {
        when(homeState) {
            is Resource.Error -> {
                Toast.makeText(context, (homeState as Resource.Error).message, Toast.LENGTH_SHORT).show()
            }else -> {}
        }
    }


    LaunchedEffect(Unit) {
        homeVm.startGameState.collect { resource ->
            when(resource) {
                is Resource.Idle -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    singlePlayerVm.setSelectedCountry(homeVm.countryState.value)
                    singlePlayerVm.setTrueLocation(homeVm.trueLocation.first,
                        homeVm.trueLocation.second)
                    singlePlayerVm.setSelectedCity(homeVm.selectedCity)
                    singlePlayerVm.clearGameHistory()
                    navController.navigate(Screen.LoadingScreenSinglePlayer.route)
                }
                is Resource.Error -> {
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    // EVENT
    LaunchedEffect(Unit) {
        homeVm.homeEvent.collect { event ->
            when(event) {
                is HomeEvent.StartGame -> {
                    homeVm.startGame()
                }
                is HomeEvent.ShowCountryBottomSheet -> {
                    showBottomSheet = true
                }
                is HomeEvent.BackPressed -> {
                    showDialogBackPressed = true
                }
            }
        }
    }

    BackHandler {
        homeVm.onBackPressedEvent()
    }

    Column(
        Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth().heightIn(min = screenHeight),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            if (!isLandscape) {
                LottieAnimation(
                    modifier = Modifier
                        .size(170.dp)
                        .align(Alignment.CenterHorizontally),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    alignment = Alignment.Center
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(textAlign = TextAlign.Center),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Green41B
                        )
                    ) {
                        append("Geo")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Black1212
                        )
                    ) {
                        append("Hunt")
                    }
                }
            )

            Text(
                text = stringResource(R.string.how_good_is_your_geography),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = Poppins, fontSize = 12.sp, color = Black1212
                ),
            )

            Spacer(Modifier.height(30.dp))

            CustomTextField(
                true, stringResource(R.string.username), Color.White,
                16.sp, false, Black1212, Black1212,
                Black1212, 10.sp, usernameState, false, 1, {
                }, {
                    homeVm.setUserName(it)
                }, true
            )

            Spacer(Modifier.height(12.dp))

            CustomTextField(
                true, stringResource(R.string.pick_a_country), Color.White,
                16.sp, true, Black1212, Black1212,
                Black1212, 10.sp, countryState.name, true,
                1, {
                    homeVm.showCountryBottomSheet()
                }, {})

            Spacer(Modifier.height(28.dp))

            CustomButton(
                buttonColor, 16.sp, Black1212,
                FontWeight.Medium, Color.White, buttonText, {
                    homeVm.startGameEvent()
                })
        }
    }


}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GeoHuntTheme {
        HomeScreen()
    }
}