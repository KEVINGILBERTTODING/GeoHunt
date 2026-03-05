package com.geohunt.presentation.map.mp.game.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.core.extension.formatTime
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Poppins

@Composable
fun TimeContainer(time: Int = 0) {
    val infiniteTransition = rememberInfiniteTransition()
    val heartbeatScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (time <= 15) 1.3f else 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                1f   at 0
                1.3f at 100
                1f   at 400
                1f   at 1000
            },
            repeatMode = RepeatMode.Restart
        )
    )

    val timerColor = when {
        time <= 15 -> Color(0xFFFF3B3B)
        else -> Color.White
    }
    Box(Modifier
        .width(100.dp)
        .graphicsLayer {
            alpha = 0.9f
            clip = true
            shape = RoundedCornerShape(16.dp)
        }
        .background(
            color = Color.White.copy(alpha = 0.15f),
            shape = RoundedCornerShape(16.dp)
        )
        .border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.3f),
            shape = RoundedCornerShape(16.dp)
        )) {

        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = if (time <= 15) heartbeatScale else 1f,
                    scaleY = if (time <= 15) heartbeatScale else 1f
                ),
            text = time.formatTime(),
            color = timerColor,
            fontSize = 20.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeContainerPreview() {
    GeoHuntTheme {
        TimeContainer(0)
    }
}