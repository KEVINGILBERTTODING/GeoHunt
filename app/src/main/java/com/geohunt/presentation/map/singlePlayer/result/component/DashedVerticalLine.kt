package com.geohunt.presentation.map.singlePlayer.result.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashedVerticalLine(
    color: Color = Color.Gray,
    dashHeight: Float = 10f,
    gapHeight: Float = 5f,
    lineHeight: Dp = 100.dp,
    lineWidth: Dp = 2.dp
) {
    Canvas(
        modifier = Modifier
            .height(lineHeight)
            .width(lineWidth)
    ) {
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(0f, y + dashHeight),
                strokeWidth = size.width
            )
            y += dashHeight + gapHeight
        }
    }
}
