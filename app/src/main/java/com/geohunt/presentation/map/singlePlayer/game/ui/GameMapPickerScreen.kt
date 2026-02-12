package com.geohunt.presentation.map.singlePlayer.game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.Green41B
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.MarkerState.Companion.invoke
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun GameMapPickerScreen(onDismiss: () -> Unit,
                        onClick: (Pair<String, String>) -> Unit) {
    var markerPosition by remember {
        mutableStateOf<LatLng?>(null)
    }

    val cameraPositionState = rememberCameraPositionState()
    Box(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    markerPosition = latLng
                }
            ) {

                markerPosition?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Selected location"
                    )
                }
            }
        }

        Box(Modifier.align(Alignment.BottomCenter)
            .fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 20.dp, start = 16.dp, end = 16.dp).fillMaxWidth()) {

                Box(Modifier.weight(1f)) {
                    CustomButton(
                        Green41B, 16.sp, Black39,
                        FontWeight.Medium, Color.White, "Pick", {
                            onClick(Pair("", ""))
                            onDismiss()
                        })
                }

                Box(Modifier.weight(1f)) {
                    CustomButton(
                        Green41B, 16.sp, Black39,
                        FontWeight.Medium, Color.White, "Pick", {
                            onClick(Pair("", ""))
                            onDismiss()
                        })
                }
            }

        }
    }
}