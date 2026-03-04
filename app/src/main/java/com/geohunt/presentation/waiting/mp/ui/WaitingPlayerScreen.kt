package com.geohunt.presentation.waiting.mp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.geohunt.core.ui.theme.White

@Composable
fun WaitingPlayerScreen() {
    Box(Modifier.fillMaxSize().background(color = White)) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "waiting player")
    }
}