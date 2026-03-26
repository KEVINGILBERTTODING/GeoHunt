package com.geohunt.presentation.waiting.mp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.geohunt.R
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.presentation.home.ui.HomeScreen

@Composable
fun WaitingPlayerScreen() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading_2)
    )
    Box(Modifier.fillMaxSize().background(color = White)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .align(Alignment.Center)
        ) {
            LottieAnimation(
                modifier = Modifier
                    .size(170.dp)
                    .align(Alignment.CenterHorizontally),
                composition = composition,
                iterations = LottieConstants.IterateForever,
                alignment = Alignment.Center)
            Text(
                modifier = Modifier.fillMaxWidth(),
                color = Black39,
                fontSize = 14.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                text = stringResource(R.string.waiting_player)
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun WaitingPlayerScreenPreview() {
    GeoHuntTheme {
      WaitingPlayerScreen()
    }
}
