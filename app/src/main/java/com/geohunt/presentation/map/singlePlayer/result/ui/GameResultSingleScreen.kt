package com.geohunt.presentation.map.singlePlayer.result.ui

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.geohunt.core.navigation.Screen
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.White
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.map.singlePlayer.result.vm.GameResultSinglePlayerVm
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameResultSingleScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(Screen.HomeGraph.route)
    }

    val singlePlayerVm: SinglePlayerVm = hiltViewModel(parentEntry)
    val trueLocationState by singlePlayerVm.trueLocation.collectAsStateWithLifecycle()
    val trueLocationMarkerState = rememberMarkerState(position = LatLng(
        trueLocationState.first.toDouble(),
        trueLocationState.second.toDouble())
        )

    val guessedLocationState by singlePlayerVm.guessedLocation.collectAsStateWithLifecycle()
    val resultVm: GameResultSinglePlayerVm = hiltViewModel()
    val scoreState by resultVm.totalScore.collectAsStateWithLifecycle()
    val distanceState by resultVm.totalDistancePrettier.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        resultVm.countDistanceAndScore(trueLocationState, guessedLocationState)
    }

    val guessedLocationMarkerState = rememberMarkerState(position = LatLng(
        guessedLocationState.first.toDouble(),
        guessedLocationState.second.toDouble())
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                trueLocationState.first.toDouble(),
                trueLocationState.second.toDouble()), 18f)
    }
    val listMarker = mutableListOf(
        LatLng(
            trueLocationState.first.toDouble(),
            trueLocationState.second.toDouble()),
        LatLng(
            guessedLocationState.first.toDouble(),
            guessedLocationState.second.toDouble())
    )



    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        sheetContainerColor = White,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Green41B
            )
        },
        sheetContent = {
            Column() {
                Text("Hasil akhir")
                Text("Score: $scoreState")
                Text("Distance: $distanceState")
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
                    icon = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )

                Marker(
                    state = guessedLocationMarkerState,
                    title = "Guessed Location",
                    tag = "guessedLocation",

                    )

                Polyline(
                    points = listMarker,
                    color = White,
                    width = 14f
                )


                Polyline(
                    points = listMarker,
                    color = Black1212,
                    width = 8f
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ResultMapSingleScreenPreview() {
    GeoHuntTheme {

    }
}