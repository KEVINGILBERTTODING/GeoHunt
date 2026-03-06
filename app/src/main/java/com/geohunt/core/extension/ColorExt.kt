package com.geohunt.core.extension

import androidx.compose.ui.graphics.Color

fun Color.onColor(): Color {
    val luminance = (0.299 * red + 0.587 * green + 0.114 * blue)
    return if (luminance > 0.5f) Color.Black else Color.White
}