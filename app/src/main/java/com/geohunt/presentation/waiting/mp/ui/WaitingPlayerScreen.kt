package com.geohunt.presentation.waiting.mp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.geohunt.R
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White

@Composable
fun WaitingPlayerScreen() {
    Box(Modifier.fillMaxSize().background(color = White)) {
        Text(
            color = Black39,
            fontSize = 14.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.waiting_player)
        )
    }
}