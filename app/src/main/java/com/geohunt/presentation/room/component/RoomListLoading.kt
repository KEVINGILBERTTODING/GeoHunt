package com.geohunt.presentation.room.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoomListLoading() {
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(Modifier.size(50.dp).align(Alignment.Center))
    }
}