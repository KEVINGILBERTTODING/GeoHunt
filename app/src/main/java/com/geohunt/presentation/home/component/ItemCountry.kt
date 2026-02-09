package com.geohunt.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GreenE6
import com.geohunt.core.ui.theme.Poppins

@Composable
fun ItemCountry(bgColor: Color, fontSize: TextUnit, borderColor: Color,
                 fontWeight: FontWeight, fontColor: Color, value: String, textAlign: TextAlign,
                 onClick: () -> Unit = {}) {
    Box(Modifier.fillMaxWidth()
        .height(56.dp)
        .background(
            color = borderColor,
            shape = RoundedCornerShape(16.dp)))
    {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp, top = 1.dp, end = 1.dp, start = 1.dp)
                .height(56.dp)
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(16.dp))
                .clickable(onClick = {
                    onClick()
                })
        ) {
            Text(
                modifier = Modifier.padding(8.dp).fillMaxWidth().align(Alignment.Center),
                text = value,
                fontSize = fontSize,
                color = fontColor,
                fontFamily = Poppins,
                fontWeight = fontWeight,
                textAlign = textAlign

            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun TextContainerPreview() {
    GeoHuntTheme {
        ItemCountry (Color.White, 16.sp, GreenE6,
            FontWeight.Normal, Black1212, "Login",
            TextAlign.Start, {})
    }
}