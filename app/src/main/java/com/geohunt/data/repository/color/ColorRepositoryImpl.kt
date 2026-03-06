package com.geohunt.data.repository.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.domain.repository.ColorRepository
import javax.inject.Inject

class ColorRepositoryImpl @Inject constructor(): ColorRepository {
    val playerMarkerColors = listOf(
        Color(0xFF2196F3), // Player 1 - Biru
        Color(0xFFE91E63), // Player 2 - Pink
        Color(0xFFFF9800), // Player 3 - Orange
        Color(0xFF9C27B0), // Player 4 - Ungu
        Color(0xFF00BCD4), // Player 5 - Cyan
        Color(0xFFF44336), // Player 6 - Merah
        Color(0xFFFF4081), // Player 7 - Magenta
        Color(0xFF03A9F4), // Player 8 - Biru Muda
        Color(0xFFFFEB3B), // Player 9 - Kuning
        Color(0xFFFF5722), // Player 10 - Deep Orange
    )
    override fun getColor(index: Int): Int {
        return playerMarkerColors[index].toArgb()
    }

    override fun getTrueLocationColor(index: Int): Int {
        return Green41B.toArgb()
    }
}