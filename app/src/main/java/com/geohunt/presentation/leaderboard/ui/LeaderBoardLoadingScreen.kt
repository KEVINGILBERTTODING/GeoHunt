package com.geohunt.presentation.leaderboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B

@Composable
fun LeaderBoardLoadingScreen() {
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.size(30.dp).align(Alignment.Center),
            color = Green41B
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderBoardScreenLoadingScreenPreview() {
    GeoHuntTheme {
        LeaderBoardLoadingScreen()
    }
}