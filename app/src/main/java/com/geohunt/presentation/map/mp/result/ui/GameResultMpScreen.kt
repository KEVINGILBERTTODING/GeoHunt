package com.geohunt.presentation.map.mp.result.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.geohunt.R
import com.geohunt.core.contract.MultiPlayerIntent
import com.geohunt.core.contract.MultiPlayerUiState
import com.geohunt.core.extension.bitmapDescriptorFromVector
import com.geohunt.core.navigation.Screen
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.core.vm.multiPlayer.MultiPlayerVm
import com.geohunt.domain.model.Answer
import com.geohunt.domain.model.LeaderBoard
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Room
import com.geohunt.domain.model.Round
import com.geohunt.domain.model.RoundResult
import com.geohunt.presentation.map.mp.result.component.ItemLeaderBoard
import com.geohunt.presentation.map.mp.result.component.ItemRoundResult
import com.geohunt.presentation.map.mp.result.contract.GameResultMpIntent
import com.geohunt.presentation.map.mp.result.contract.GameResultMpUiState
import com.geohunt.presentation.map.mp.result.vm.GameResultMpVm
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import kotlinx.coroutines.launch

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
    val trueLoc = remember(uiState.round.trueLat, uiState.round.trueLng) {
        LatLng(uiState.round.trueLat.toDoubleOrNull() ?: 0.0,
            uiState.round.trueLng.toDoubleOrNull() ?: 0.0)
    }


    BackHandler {
        showBottomSheetBack = true
    }


    LaunchedEffect(uiState.round) {
        uiState.cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(uiState.round.trueLat.toDoubleOrNull() ?: 0.0,
                        uiState.round.trueLng.toDoubleOrNull() ?: 0.0),
                    15f
                )
            )
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

                uiState.roundResultList.forEachIndexed { index, answer ->
                    val guessedLoc = remember(answer.lat, answer.lng) {
                        LatLng(answer.lat.toDoubleOrNull() ?: 0.0,
                            uiState.round.trueLng.toDoubleOrNull() ?: 0.0)
                    }
                    Marker(
                        state = remember(guessedLoc) {
                            MarkerState(guessedLoc)
                        },
                        title = answer.player.username,
                        tag = answer.player.username,
                        icon = context.bitmapDescriptorFromVector(R.drawable.ic_marker,
                            answer.player.playerColor, 56)
                    )

                    Polyline(
                        points = listOf(guessedLoc, trueLoc),
                        color = White,
                        width = 14f
                    )

                    Polyline(
                        points = listOf(guessedLoc, trueLoc),
                        color = Black1212,
                        width = 8f
                    )
                }


                // true loc
                Marker(
                    state = remember(trueLoc) {
                        MarkerState(trueLoc)
                    },
                    title = "True Location",
                    tag = "trueLocation",
                    icon = context.bitmapDescriptorFromVector(R.drawable.ic_marker,
                        uiState.trueLocColor, 56)
                )


            }
        }
    }

}

@Composable
fun GameResultMpContent(uiState: GameResultMpUiState, mpUiState: MultiPlayerUiState,
                        gameResultIntent : (GameResultMpIntent) -> Unit,
                        mpIntent : (MultiPlayerIntent) -> Unit,
                        ) {
    val tabs = listOf(stringResource(R.string.round_result), stringResource(R.string.leaderboard))
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { tabs.size })


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp, top = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
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
                            append("+${uiState.point}")
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
            TabRow(
                modifier = Modifier
                    .fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, string ->
                    Tab(
                        modifier = Modifier.background(White),
                        selected = pagerState.currentPage ==  index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = Green41B,
                        unselectedContentColor = Color.Gray,
                        text = {
                            Text(
                            text = string,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                ) { page ->
                when(page) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp),
                            verticalArrangement = Arrangement.Top) {
                            items(uiState.roundResultList) { item ->
                                ItemRoundResult(item)
                                Spacer(Modifier.height(15.dp))
                            }
                        }
                    }
                    1 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp),
                            verticalArrangement = Arrangement.Top)
                        {
                            items(uiState.leaderBoardList) {
                                ItemLeaderBoard(it)
                                Spacer(Modifier.height(15.dp))
                            }
                        }
                    }
                }
            }

        }
    }
}



@Preview(showBackground = true)
@Composable
fun ResultMapMpPreview() {
    val roundResultList = listOf(
        RoundResult(
            player = Player(username = "kevin"),
            lat = "123.456789",
            lng = "987.654321",
            point = 100,
            distance = 20f
        ),
        RoundResult(
            player = Player(username = "kevin"),
            lat = "123.456789",
            lng = "987.654321",
            point = 100,
            distance = 20f
        ),

    )

    val leaderBoardList = listOf(
        LeaderBoard(
            player = Player("1", "kevin", playerColor = Color(0xFF9C27B0).toArgb()),
            totalPoint = 2230,
            rank = 1
        ),
        LeaderBoard(
            player = Player("1", "kevin", playerColor = Color(0xFF9C27B0).toArgb()),
            totalPoint = 2230,
            rank = 1
        ),
        LeaderBoard(
            player = Player("1", "kevin", playerColor = Color(0xFF9C27B0).toArgb()),
            totalPoint = 2230,
            rank = 1
        ),
    )

    GeoHuntTheme {
        GameResultMpContent(
            GameResultMpUiState(roundResultList = roundResultList, leaderBoardList = leaderBoardList),
            MultiPlayerUiState(),{}, {}
        )
    }
}