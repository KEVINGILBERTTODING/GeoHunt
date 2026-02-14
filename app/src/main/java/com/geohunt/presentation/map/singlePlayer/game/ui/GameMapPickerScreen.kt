package com.geohunt.presentation.map.singlePlayer.game.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geohunt.R
import com.geohunt.core.extension.bitmapDescriptorFromVector
import com.geohunt.core.resource.Resource
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.component.CustomFab
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.BlueE6
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Orange
import com.geohunt.core.util.MapGraphicUtil
import com.geohunt.presentation.home.ui.HomeScreen
import com.geohunt.presentation.map.singlePlayer.game.vm.GameMapSinglePlayerVm
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@SuppressLint("DefaultLocale")
@Composable
fun GameMapPickerScreen(
    viewModel: GameMapSinglePlayerVm,
    onDismiss: () -> Unit, onClick: (Pair<String, String>) -> Unit) {
    val markerLocationState by viewModel.markerPositionState.collectAsStateWithLifecycle()

    val cameraPositionState by viewModel.cameraPositionState.collectAsStateWithLifecycle()

    val textLatLng = if (markerLocationState == null) {
        stringResource(R.string.tap_on_the_map_to_choose)
    }else {
        val latRound = String.format("%.3f", markerLocationState!!.latitude)
        val lngRound = String.format("%.3f", markerLocationState!!.longitude)
        "${latRound}, $lngRound"
    }

    val isUserHasSelectedLocation = markerLocationState != null

    val context = LocalContext.current

    var isShowBottomContainer by remember { mutableStateOf(true) }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            mapToolbarEnabled = false
        )
    }

    BackHandler {
        onDismiss()
    }

    val buttonColor = if (isUserHasSelectedLocation) Green41B else Color.Gray
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            uiSettings = uiSettings,
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                viewModel.setMarkerPositionState(latLng)
                isShowBottomContainer = true
            }
        ) {

            markerLocationState?.let {
                Marker(
                    icon = context.bitmapDescriptorFromVector(R.drawable.ic_marker,
                        Orange.toArgb(), 56),
                    state = MarkerState(position = it),
                    title = "Selected location"
                )
            }
        }

        if (!isShowBottomContainer) {
            Box(Modifier.align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 25.dp)) {
                CustomFab(
                    image = painterResource(R.drawable.ic_up),
                    colorTint = Black1212
                ) {
                    isShowBottomContainer = true
                }
            }
        }

        Column(
            Modifier.align(Alignment.BottomCenter)
            .fillMaxWidth()
        ) {
           if (isShowBottomContainer) {
               Box(Modifier.align(Alignment.End).padding(end = 16.dp)) {
                   CustomFab(
                       image = painterResource(R.drawable.ic_close),
                       colorTint = Black1212
                   ) {
                       isShowBottomContainer = false
                   }
               }
           }

            Spacer(Modifier.height(20.dp))

            if (isShowBottomContainer) {
                Box(Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )) {
                    Column(Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp, start = 16.dp, end = 16.dp, top = 25.dp)) {

                        CustomTextField(true, stringResource(R.string.coordinate),
                            Color.White,14.sp, true, Black1212,
                            Black1212,Black1212, 10.sp, textLatLng,
                            false, 3)

                        Spacer(Modifier.height(20.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Box(Modifier.weight(1f)) {
                                CustomButton(
                                    Color.White, 14.sp, Black1212,
                                    FontWeight.Medium, Black1212, stringResource(R.string.street_view), {
                                        onDismiss()
                                    })
                            }

                            Box(Modifier.weight(1f)) {
                                CustomButton(
                                    buttonColor, 14.sp, Black1212,
                                    FontWeight.Medium, Color.White, stringResource(R.string.confirm), {
                                        if (isUserHasSelectedLocation) {
                                            onClick(Pair(markerLocationState!!.latitude.toString(),
                                                markerLocationState!!.longitude.toString()))
                                        }else {
                                            Toast.makeText(context,
                                                context.getString(R.string.no_location_selected),
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }
                    }

                }
            }
        }


    }

}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun GameMapPickerPreview() {
    GeoHuntTheme {
        GameMapPickerScreen(GameMapSinglePlayerVm(), {}) { }
    }
}