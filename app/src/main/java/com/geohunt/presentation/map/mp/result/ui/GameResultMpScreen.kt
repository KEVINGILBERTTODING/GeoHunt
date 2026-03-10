package com.geohunt.presentation.map.mp.result.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.geohunt.R
import com.geohunt.core.contract.MultiPlayerIntent
import com.geohunt.core.contract.MultiPlayerUiState
import com.geohunt.core.navigation.Screen
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.core.vm.multiPlayer.MultiPlayerVm
import com.geohunt.presentation.map.mp.result.component.ItemGameHistoryMp
import com.geohunt.presentation.map.mp.result.contract.GameResultMpIntent
import com.geohunt.presentation.map.mp.result.contract.GameResultMpUiState
import com.geohunt.presentation.map.mp.result.vm.GameResultMpVm
import com.google.maps.android.compose.GoogleMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameResultMpScreen(
    navigateToHome: () -> Unit,
    navigateToMap: () -> Unit,
    mpVm: MultiPlayerVm,
    vm: GameResultMpVm = hiltViewModel()
) {
    val uiState by vm.state.collectAsStateWithLifecycle()
    val mpUiState by mpVm.state.collectAsStateWithLifecycle()
    var showBottomSheetBack by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()



//    LaunchedEffect(trueLocationState, guessedLocationState) {
//        if (trueLocationState.first.isNotBlank() && trueLocationState.second.isNotBlank()
//            && guessedLocationState.first.isNotBlank() && guessedLocationState.second.isNotBlank()) {
//            trueLocationMarkerState.position = LatLng(
//                trueLocationState.first.toDouble(),
//                trueLocationState.second.toDouble()
//            )
//            guessedLocationMarkerState.position = LatLng(
//                guessedLocationState.first.toDouble(),
//                guessedLocationState.second.toDouble()
//            )
//            listMarker.clear()
//            listMarker.add(LatLng(
//                trueLocationState.first.toDouble(),
//                trueLocationState.second.toDouble()))
//            listMarker.add(LatLng(
//                guessedLocationState.first.toDouble(),
//                guessedLocationState.second.toDouble()))
//
//            cameraPositionState.animate(
//                update = CameraUpdateFactory.newLatLngZoom(
//                    LatLng(trueLocationState.first.toDouble(), trueLocationState.second.toDouble()),
//                    resultVm.getLevelZoom(trueLocationState, guessedLocationState)
//                )
//            )
//        }
//    }



    BackHandler {
        showBottomSheetBack = true
    }

    if (showBottomSheetBack) {
        ConfirmationBottomSheet(stringResource(R.string.return_to_home),
            stringResource(R.string.are_you_sure_you_want_to_return_to_home),
            stringResource(R.string.yes_return_to_home),
            stringResource(R.string.cancel),
            true, true,
            {
                showBottomSheetBack = false
            },
            {
                showBottomSheetBack = false
//                resultVm.navigateToHome()
            }) {
            showBottomSheetBack = false
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 200.dp,
        sheetContainerColor = White,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = GrayE0
            )
        },
        sheetContent = {
            GameResultMpContent(uiState, mpUiState, {

            }, {

            })
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = uiState.cameraPositionState,
                onMapClick = { latLng ->

                }
            ) {

//                Marker(
//                    state = trueLocationMarkerState,
//                    title = "True Location",
//                    tag = "trueLocation",
//                    icon = context.bitmapDescriptorFromVector(R.drawable.ic_marker,
//                        BlueE6.toArgb(), 56)
//                )
//
//                Marker(
//                    state = guessedLocationMarkerState,
//                    title = "Guessed Location",
//                    tag = "guessedLocation",
//                    icon = context.bitmapDescriptorFromVector(R.drawable.ic_marker,
//                        Orange.toArgb(), 56)
//                    )
//
//                Polyline(
//                    points = listMarker.toList(),
//                    color = White,
//                    width = 14f
//                )
//
//
//                Polyline(
//                    points = listMarker.toList(),
//                    color = Black1212,
//                    width = 8f
//                )
            }
        }
    }

}

@Composable
fun GameResultMpContent(uiState: GameResultMpUiState, mpUiState: MultiPlayerUiState,
                        gameResultIntent : (GameResultMpIntent) -> Unit,
                        mpIntent : (MultiPlayerIntent) -> Unit,
                        ) {
    val answer = uiState.room.rounds.last().answers.find {
        it.uid == uiState.userData.userId }

    Box(
        modifier = Modifier
            .background(White)
    ) {
        LazyColumn(Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 90.dp, top = 10.dp)) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(textAlign = TextAlign.Center),
                    text =
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 40.sp,
                                    color = Green41B,
                                )
                            ) {
                                        append("+${answer?.point ?: 0}")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Black1212,
                                )
                            ) {
                                append(" ${stringResource(R.string.points)}")
                            }
                        }
                )
                Spacer(Modifier.height(10.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.game_history),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Black1212,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(Modifier.height(10.dp))
            }

            items(uiState.room.rounds.last().answers
                .sortedByDescending { it.point }.map { it }) { item ->
                ItemGameHistoryMp(item, uiState.room)
                Spacer(Modifier.height(15.dp))
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun ResultMapSingleScreenPreview() {
    GeoHuntTheme {
        GameResultMpContent(
            GameResultMpUiState(),
            MultiPlayerUiState(),{}, {}
        )
    }
}