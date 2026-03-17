package com.geohunt.presentation.loadingScreen.singlePlayer.ui

import android.R.attr.resource
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.loadingScreen.singlePlayer.event.LoadingSinglePlayerEvent
import com.geohunt.presentation.loadingScreen.singlePlayer.vm.LoadingSinglePlayerVm

@Composable
fun LoadingSinglePlayerScreen(
    singlePlayerVm: SinglePlayerVm,
    onBack: () -> Unit,
    onNavigateToMap: () -> Unit
){
    val context = LocalContext.current
    val loadingSinglePlayerVm: LoadingSinglePlayerVm =  hiltViewModel()
    val loadingMsg by loadingSinglePlayerVm.loadingMsg.collectAsStateWithLifecycle()
    val tipsMsg by loadingSinglePlayerVm.tipsMessage.collectAsStateWithLifecycle()
    var showDialogBackPressed by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        singlePlayerVm.getPhotos()
    }

    LaunchedEffect(Unit) {
        loadingSinglePlayerVm.loadingSinglePlayerEvent.collect { event ->
            when(event) {
                is LoadingSinglePlayerEvent.navigateToMap -> {
                    onNavigateToMap()
                }
                is LoadingSinglePlayerEvent.NavigateToHome -> {
                    showDialogBackPressed = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        singlePlayerVm.loadingState.collect { state ->
            when (state) {
                is Resource.Idle -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    loadingSinglePlayerVm.navigateToMap()
                }
                is Resource.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    onBack()
                }
            }
        }
    }

    if (showDialogBackPressed) {
        ConfirmationBottomSheet(stringResource(R.string.return_to_home),
            stringResource(R.string.are_you_sure_you_want_to_return_to_home),
            stringResource(R.string.yes_return_to_home),
            stringResource(R.string.cancel),
            true, true,
            {
                showDialogBackPressed = false
            },
            {
                showDialogBackPressed = false
                onBack()
            }) {
            showDialogBackPressed = false
        }
    }

    BackHandler {
        loadingSinglePlayerVm.navigateToHome()
    }

    Content(loadingMsg, tipsMsg)
}

@Composable
private fun Content(loadingMsg: String, tipsMsg: String) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    Box(Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(Modifier.weight(1f),
                verticalArrangement = Arrangement.Center) {
                LottieAnimation(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .size(200.dp),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    alignment = Alignment.Center
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    text = loadingMsg,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    color = Black1212
                )
            }

            Box(
                Modifier.padding(bottom = 20.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                CustomTextField(true, stringResource(R.string.tips_game), Color.White,
                    14.sp, true, Black1212, Black1212,
                    Black1212, 10.sp, tipsMsg, false, 3)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingSinglePlayerPreview() {
    GeoHuntTheme {
        Content("test", "test")
    }
}