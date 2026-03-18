package com.geohunt.presentation.loadingScreen.multiplayer.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.geohunt.R
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.presentation.loadingScreen.multiplayer.contract.LoadingMpUiState
import com.geohunt.presentation.loadingScreen.multiplayer.vm.LoadingMpVm

@Composable
fun LoadingMpScreen(
    vm: LoadingMpVm = hiltViewModel()
){
    val uiState by vm.state.collectAsStateWithLifecycle()
    BackHandler {

    }
    Content(uiState)


}
@Composable
private fun Content(uiState: LoadingMpUiState) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )
    Box(Modifier.fillMaxSize().background(White)) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(Modifier.weight(1f).padding(16.dp),
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
                    text = uiState.loadingMsg,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    color = Black1212
                )
            }

            Box(
                Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 20.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                CustomTextField(true, stringResource(R.string.tips_game), Color.White,
                    14.sp, true, Black1212, Black1212,
                    Black1212, 10.sp, uiState.tipsMsg, false, 3)
            }

        }
    }
}
@Preview(showBackground = true)
@Composable
fun LoadingSinglePlayerPreview() {
    GeoHuntTheme {
        Content(LoadingMpUiState(tipsMsg = "test", loadingMsg = "test"))
    }
}