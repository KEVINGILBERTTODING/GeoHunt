package com.geohunt.presentation.loadingScreen.singlePlayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.geohunt.R
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme

@Composable
fun LoadingSinglePlayerScreen(navController: NavController = rememberNavController()){
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    Box(Modifier.fillMaxSize().padding(16.dp)) {
        LottieAnimation(
            modifier = Modifier.size(200.dp).align(Alignment.Center),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            alignment = Alignment.Center
        )

        Box(Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)) {
            CustomTextField(true, "Loading", Color.White,
                16.sp, true, Black39, Black1212,
                Black1212, 10.sp, "Preparing your adventure…", 1)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingSinglePlayerPreview() {
    GeoHuntTheme {
        LoadingSinglePlayerScreen()
    }
}