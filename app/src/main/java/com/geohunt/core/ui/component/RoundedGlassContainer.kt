package com.geohunt.core.ui.component
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geohunt.R
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.White

@Composable
fun RoundedGlassContainer(icon: Int, onClick : () -> Unit = {}) {
    Box(Modifier
        .size(48.dp)
        .graphicsLayer {
            alpha = 0.9f
            clip = true
            shape = CircleShape
        }
        .background(
            color = Color.White.copy(alpha = 0.15f),
            shape = CircleShape
        )
        .border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.3f),
            shape = CircleShape
        )
        .clickable(
            onClick = {
                onClick()
            }
        )
    ) {
        Image(
            modifier = Modifier.size(24.dp)
                .align(Alignment.Center),
            painter = painterResource(icon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedGlassContainerPreview() {
    GeoHuntTheme {
        RoundedGlassContainer(R.drawable.ic_arrow)
    }
}