package com.geohunt.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GreenE6
import com.geohunt.core.ui.theme.Poppins

@Composable
fun CustomTextField(isEnabled: Boolean = false, label: String = "", bgColor: Color,
                    fontSize: TextUnit, isReadOnly: Boolean, borderColor: Color,
                    fontColor: Color, labelColor: Color, labelSize: TextUnit, value: String,
                    maxLines: Int, onClick: () -> Unit = {}) {
    Box(Modifier.fillMaxWidth()
        .background(
            color = borderColor,
            shape = RoundedCornerShape(16.dp)))
    {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp, top = 1.dp, end = 1.dp, start = 1.dp)
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(16.dp))
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value,
                onValueChange = {},
                maxLines = maxLines,
                enabled = isEnabled,
                readOnly = isReadOnly,
                textStyle = TextStyle(fontSize = fontSize, color = fontColor),
                label = {
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = label,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = labelSize,
                        color = labelColor
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = Black1212,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { onClick() }
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun TextFieldPreview() {
    GeoHuntTheme {
       CustomTextField(true, "Pilih negara", Color.White,
           16.sp, true, GreenE6, Black1212, Black1212, 14.sp,
           "Indonesia", 1)
    }
}