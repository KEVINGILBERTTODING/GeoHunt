package com.geohunt.presentation.map.mp.game.ui

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geohunt.R
import com.geohunt.core.contract.MultiPlayerUiState
import com.geohunt.core.extension.bitmapDescriptorFromVector
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.component.CustomFab
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Orange
import com.geohunt.presentation.map.mp.game.contract.GameMapMpPickerIntent
import com.geohunt.presentation.map.mp.game.contract.GameMapMpPickerUiState
import com.geohunt.presentation.map.mp.game.contract.GameMapMpUiState
import com.geohunt.presentation.map.mp.game.vm.GameMapMpPickerVm
import com.geohunt.presentation.map.singlePlayer.game.vm.GameMapSinglePlayerVm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState


@Composable
fun GameMapMpPickerScreen(
    viewModel: GameMapMpPickerVm = hiltViewModel(),
    mpUiState: MultiPlayerUiState,
    onDismiss: () -> Unit) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    ContentScreen(uiState, mpUiState, { onDismiss() }, {
        viewModel.onIntent(it)
    })
}

@SuppressLint("DefaultLocale")
@Composable
private fun ContentScreen(uiState: GameMapMpPickerUiState,
                          mpUiState: MultiPlayerUiState,
                          onDismiss: () -> Unit,
                          onIntent: (GameMapMpPickerIntent) -> Unit) {
    val textLatLng = if (uiState.latLng == null) {
        stringResource(R.string.tap_on_the_map_to_choose)
    }else {
        val latRound = String.format("%.3f", uiState.latLng.latitude)
        val lngRound = String.format("%.3f", uiState.latLng.longitude)
        "${latRound}, $lngRound"
    }


    val isUserHasSelectedLocation = uiState.latLng != null

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

    val buttonColor = if (uiState.isLoadingSubmit.not()) {
        if (isUserHasSelectedLocation.not()) {
            Color.Gray
        }else {
            if (uiState.isSuccessSubmit) Color.Gray
            else Green41B
        }
    }
    else Color.Gray

    val textButton = if (uiState.isLoadingSubmit) stringResource(R.string.loading_game)
    else {
        if (uiState.isSuccessSubmit) stringResource(R.string.lock_in)
        else stringResource(R.string.confirm)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            uiSettings = uiSettings,
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = uiState.cameraPositionState,
            onMapClick = { latLng ->
                if (uiState.isSuccessSubmit.not()) {
                    onIntent(GameMapMpPickerIntent.OnSaveLatLng(latLng))
                    isShowBottomContainer = true
                }
            }
        ) {

            uiState.latLng?.let {
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
            AnimatedVisibility(
                visible = isShowBottomContainer,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Box(Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    CustomFab(
                        image = painterResource(R.drawable.ic_close),
                        colorTint = Black1212
                    ) {
                        isShowBottomContainer = !isShowBottomContainer
                    }
                }

            }


            Spacer(Modifier.height(20.dp))

            AnimatedVisibility(
                visible = isShowBottomContainer,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
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
                                    FontWeight.Medium, Color.White, textButton, {
                                        if (isUserHasSelectedLocation && uiState.isLoadingSubmit.not()
                                            && uiState.isSuccessSubmit.not()) {
                                            onIntent(GameMapMpPickerIntent.OnSubmitAnswer(
                                                mpUiState.trueLocPair, mpUiState.currentRound))
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
        ContentScreen(GameMapMpPickerUiState(), MultiPlayerUiState(),{}, {})
    }
}