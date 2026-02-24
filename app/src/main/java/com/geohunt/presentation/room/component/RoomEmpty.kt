package com.geohunt.presentation.room.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.R
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Poppins

@Composable
fun RoomEmpty() {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.data_not_found),
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontFamily = Poppins, fontSize = 12.sp, color = Black1212
            ),
        )
    }
}