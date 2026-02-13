package com.geohunt.presentation.map.singlePlayer.game.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geohunt.R
import com.geohunt.core.resource.Resource
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.presentation.map.singlePlayer.game.vm.GameMapSinglePlayerVm
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@SuppressLint("DefaultLocale")
@Composable
fun GameMapPickerScreen(
    viewModel: GameMapSinglePlayerVm,
    onDismiss: () -> Unit,
                        onClick: (Pair<String, String>) -> Unit) {
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

    BackHandler {
        onDismiss()
    }

    val buttonColor = if (isUserHasSelectedLocation) Green41B else Color.Gray
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                viewModel.setMarkerPositionState(latLng)
            }
        ) {

            markerLocationState?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Selected location"
                )
            }
        }

        Box(Modifier
            .align(Alignment.BottomCenter)
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
                            Color.White, 12.sp, Black1212,
                            FontWeight.Medium, Black1212, stringResource(R.string.street_view), {
                                onDismiss()
                            })
                    }

                    Box(Modifier.weight(1f)) {
                        CustomButton(
                            buttonColor, 12.sp, Black1212,
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