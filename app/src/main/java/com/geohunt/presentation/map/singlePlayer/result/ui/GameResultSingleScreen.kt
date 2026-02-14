package com.geohunt.presentation.map.singlePlayer.result.ui

import android.graphics.Color
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.geohunt.R
import com.geohunt.core.extension.bitmapDescriptorFromVector
import com.geohunt.core.navigation.Screen
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.BlueE6
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Orange
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.country.Country
import com.geohunt.domain.model.GameHistorySinglePlayer
import com.geohunt.presentation.map.singlePlayer.result.component.ItemGameHistorySinglePlayer
import com.geohunt.presentation.map.singlePlayer.result.event.GameResultSinglePlayerEvent
import com.geohunt.presentation.map.singlePlayer.result.vm.GameResultSinglePlayerVm
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameResultSingleScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(Screen.HomeGraph.route)
    }
    val singlePlayerVm: SinglePlayerVm = hiltViewModel(parentEntry)
    val trueLocationSingleVmState by singlePlayerVm.trueLocation.collectAsStateWithLifecycle()
    val guessedLocationSingleVmState by singlePlayerVm.guessedLocation.collectAsStateWithLifecycle()
    val gameHistory by singlePlayerVm.gameHistory.collectAsStateWithLifecycle()
    val resultVm: GameResultSinglePlayerVm = hiltViewModel()
    val trueLocationState by resultVm.trueLocationState.collectAsStateWithLifecycle()
    val guessedLocationState by resultVm.guessedLocationState.collectAsStateWithLifecycle()
    var showBottomSheetBack by remember { mutableStateOf(false) }
    val trueLocationMarkerState = rememberMarkerState()
    val guessedLocationMarkerState = rememberMarkerState()
    val cameraPositionState = rememberCameraPositionState()
    val listMarker = remember { mutableStateListOf<LatLng>() }
    val context = LocalContext.current
    var scaffoldState = rememberBottomSheetScaffoldState()
    val totalPoint = gameHistory.sumOf { it.point }
    val scope = rememberCoroutineScope()



    // EVENT
    LaunchedEffect(Unit) {
        resultVm.gameEvent.collect { event ->
            when(event) {
                is GameResultSinglePlayerEvent.NavigateToHome -> {
                    navigateToHome(navController)
                }
                is GameResultSinglePlayerEvent.ChangeMarkerState -> {
                    scope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                    resultVm.setTrueLocationState(event.trueLoc)
                    resultVm.setGuessedLocationState(event.guessLoc)
                }
                is GameResultSinglePlayerEvent.NextRound -> {
                    singlePlayerVm.nextRound()
                    navController.navigate(Screen.LoadingScreenSinglePlayer.route)
                }
            }
        }
    }

    LaunchedEffect(trueLocationSingleVmState) {
        resultVm.setTrueLocationState(trueLocationSingleVmState)
    }

    LaunchedEffect(guessedLocationSingleVmState) {
        resultVm.setGuessedLocationState(guessedLocationSingleVmState)
    }

    LaunchedEffect(trueLocationState, guessedLocationState) {
        if (trueLocationState.first.isNotBlank() && trueLocationState.second.isNotBlank()
            && guessedLocationState.first.isNotBlank() && guessedLocationState.second.isNotBlank()) {
            trueLocationMarkerState.position = LatLng(
                trueLocationState.first.toDouble(),
                trueLocationState.second.toDouble()
            )
            guessedLocationMarkerState.position = LatLng(
                guessedLocationState.first.toDouble(),
                guessedLocationState.second.toDouble()
            )
            listMarker.clear()
            listMarker.add(LatLng(
                trueLocationState.first.toDouble(),
                trueLocationState.second.toDouble()))
            listMarker.add(LatLng(
                guessedLocationState.first.toDouble(),
                guessedLocationState.second.toDouble()))

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(trueLocationState.first.toDouble(), trueLocationState.second.toDouble()),
                    resultVm.getLevelZoom(trueLocationState, guessedLocationState)
                )
            )
        }
    }



    BackHandler {
        showBottomSheetBack = true
    }

    if (showBottomSheetBack) {
        ConfirmationBottomSheet(stringResource(R.string.return_to_home),
            stringResource(R.string.are_you_sure_you_want_to_return_to_home),
            stringResource(R.string.yes_return_to_home),
            stringResource(R.string.cancel),
            {
                showBottomSheetBack = false
            },
            {
                showBottomSheetBack = false
                resultVm.navigateToHome()
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
            Box(
                modifier = Modifier
                    .background(White)
            ) {
                LazyColumn(Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 80.dp, top = 10.dp)) {
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
                                        append("+${gameHistory.last().point}")
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
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = GrayE0,
                                shape = RoundedCornerShape(16.dp)
                            )
                        ) {
                            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                                Row(Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(
                                        text = stringResource(R.string.total_points),
                                        fontSize = 12.sp,
                                        fontFamily = Poppins,
                                        color = Black1212,
                                        textAlign = TextAlign.Start,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Spacer(Modifier.width(20.dp))
                                    Text(
                                        text = totalPoint.toString(),
                                        fontSize = 14.sp,
                                        fontFamily = Poppins,
                                        color = Black1212,
                                        textAlign = TextAlign.End,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }

                            }
                        }

                        Spacer(Modifier.height(22.dp))

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


                    items(gameHistory.asReversed()) { item ->
                        ItemGameHistorySinglePlayer(item.no, item) {
                           resultVm.changeMarkerStateEvent(item.trueLocation, item.guessedLocation)
                        }
                        Spacer(Modifier.height(15.dp))
                    }

                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 25.dp, end = 16.dp, start = 16.dp)
                ) {
                    Box(Modifier.weight(1f)) {
                        CustomButton(
                            androidx.compose.ui.graphics.Color.White, 14.sp, Black1212,
                            FontWeight.Medium, Black1212, stringResource(R.string.home), {
                                resultVm.navigateToHome()
                            })
                    }

                    Box(Modifier.weight(1f)) {
                        CustomButton(
                            Green41B, 14.sp, Black1212,
                            FontWeight.Medium, androidx.compose.ui.graphics.Color.White, stringResource(R.string.next), {
                                resultVm.nextRound()
                            })
                    }
                }
            }

        }
    ) {
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->

                }
            ) {

                Marker(
                    state = trueLocationMarkerState,
                    title = "True Location",
                    tag = "trueLocation",
                    icon = context.bitmapDescriptorFromVector(R.drawable.ic_marker,
                        BlueE6.toArgb(), 56)
                )

                Marker(
                    state = guessedLocationMarkerState,
                    title = "Guessed Location",
                    tag = "guessedLocation",
                    icon = context.bitmapDescriptorFromVector(R.drawable.ic_marker,
                        Orange.toArgb(), 56)
                    )

                Polyline(
                    points = listMarker.toList(),
                    color = White,
                    width = 14f
                )


                Polyline(
                    points = listMarker.toList(),
                    color = Black1212,
                    width = 8f
                )
            }
        }
    }

}

private fun navigateToHome(navController: NavController) {
    navController.navigate(Screen.HomeScreen.route) {
        popUpTo(Screen.GameResultSinglePlayerScreen.route) {
            inclusive = true
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultMapSingleScreenPreview() {
    GeoHuntTheme {
        GameResultSingleScreen(rememberNavController())
    }
}