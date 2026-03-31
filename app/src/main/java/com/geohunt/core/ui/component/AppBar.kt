package com.geohunt.core.ui.component

import android.content.Intent
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.R
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Poppins

@Composable
fun AppBar(
    navigationIcon: Int?,
    actionIcon: Int?,
    title: String?,
    titleSize: TextUnit = 16.sp,
    titleColor : Color = Black1212,
    navigationIconClick: () -> Unit,
    actionIconClick: () -> Unit,
) {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround) {

        navigationIcon?.let {
            IconButton(
                onClick = {
                    navigationIconClick()
                }
            ){
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(navigationIcon),
                    contentDescription = ""
                )
            }
        }

        title?.let {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                text = title,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = Poppins,
                    fontSize = titleSize,
                    color = titleColor,
                    fontWeight = FontWeight.Medium
                ),
            )
        }

        actionIcon?.let {
            IconButton(
                onClick = {
                    actionIconClick()
                }
            ){
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(actionIcon),
                    contentDescription = ""
                )
            }
        }

    }
}