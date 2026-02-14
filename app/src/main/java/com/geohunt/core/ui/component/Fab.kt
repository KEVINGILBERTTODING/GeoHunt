package com.geohunt.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.R
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.GreenE6
import com.geohunt.core.ui.theme.Poppins

@Composable
fun CustomFab(image: Painter, borderColor: Color = Black1212, bgColor: Color = Color.White,
              colorTint: Color,
          onClick: () -> Unit) {
    Box(Modifier
        .size(56.dp)
        .background(
            color = borderColor,
            shape = RoundedCornerShape(16.dp)))
    {
        Box(
            modifier = Modifier
                .size(56.dp)
                .padding(bottom = 8.dp, top = 1.dp, end = 1.dp, start = 1.dp)
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(16.dp))
                .clickable(onClick = {
                    onClick()
                })
        ) {
            Image(
                modifier = Modifier.size(24.dp).align(Alignment.Center),
                painter = image,
                colorFilter = ColorFilter.tint(color = colorTint),
                contentDescription = null
            )

        }
    }
}

@Preview(showBackground = false)
@Composable
fun FabPreview() {
    GeoHuntTheme {
        CustomFab(painterResource(R.drawable.ic_eye), Green41B, Color.White, Green41B) { }
    }
}