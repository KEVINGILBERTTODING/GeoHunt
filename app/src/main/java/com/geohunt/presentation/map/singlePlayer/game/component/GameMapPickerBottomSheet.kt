package com.geohunt.presentation.map.singlePlayer.game.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.data.dto.city.City
import com.geohunt.presentation.home.vm.HomeVm
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameMapPickerBottomSheet(onDissmiss: () -> Unit) {
    val homeVm: HomeVm = hiltViewModel()
    var markerPosition by remember {
        mutableStateOf<LatLng?>(null)
    }

    val cameraPositionState = rememberCameraPositionState()
    ModalBottomSheet(
        onDismissRequest = { onDissmiss() },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden }
        ),
        containerColor = Color.White,
        dragHandle = null
    ) {
        Box(Modifier.fillMaxSize()) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun CountryBottomSheetPreview() {
    GeoHuntTheme {
        GameMapPickerBottomSheet {  }
    }
}