package com.geohunt.presentation.leaderboard.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geohunt.R
import com.geohunt.core.ui.component.AppBar
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.White
import com.geohunt.domain.model.LeaderBoard
import com.geohunt.domain.model.Player
import com.geohunt.presentation.leaderboard.component.ItemLeaderBoardNotPodium
import com.geohunt.presentation.leaderboard.component.LeaderBoardBar
import com.geohunt.presentation.leaderboard.component.LeaderBoardPodium
import com.geohunt.presentation.leaderboard.contract.LeaderBoardEffect
import com.geohunt.presentation.leaderboard.contract.LeaderBoardState
import com.geohunt.presentation.leaderboard.contract.LeaderBoardUiState
import com.geohunt.presentation.leaderboard.vm.LeaderBoardVm

@Composable
fun LeaderBoardScreen(
    onNavigateToHome: () -> Unit,
    leaderBoardVm: LeaderBoardVm = hiltViewModel()
) {
    val uiState by leaderBoardVm.state.collectAsStateWithLifecycle()
    val ctx = LocalContext.current


    // effect
    LaunchedEffect(Unit) {
        leaderBoardVm.effect.collect { effect ->
            when(effect) {
                is LeaderBoardEffect.ShowToast -> {
                    Toast.makeText(ctx, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    LeaderBoardContent(
        uiState = uiState,
        onNavigateToHome = onNavigateToHome
    )
}

@Composable
private fun LeaderBoardContent(
    uiState: LeaderBoardUiState,
    onNavigateToHome: () -> Unit
) {
    Box(Modifier
        .fillMaxWidth()
        .systemBarsPadding()
        .padding(16.dp)) {
        Column(Modifier.fillMaxSize()) {
            AppBar(
                actionIcon = null,
                navigationIcon = R.drawable.ic_arrow,
                title = stringResource(R.string.leaderboard),
                navigationIconClick = {
                    onNavigateToHome()
                },
                actionIconClick = {}
            )
            Spacer(Modifier.height(20.dp))

            when(uiState.leaderBoardState) {
                LeaderBoardState.Loading -> {
                    LeaderBoardLoadingScreen()
                }
                LeaderBoardState.Success -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f).navigationBarsPadding()
                    ) {
                        item {
                            LeaderBoardPodium(uiState.leaderBoardPodiumList)
                        }
                        item {
                            Spacer(Modifier.height(25.dp))
                        }
                        items(uiState.leaderBoardNotPodiumList) { leaderBoard ->
                            ItemLeaderBoardNotPodium(leaderBoard)
                            Spacer(Modifier.height(16.dp)
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LeaderBoardScreenPreview() {
    val leaderBoardList = listOf(
        LeaderBoard(
            player = Player(username = "Jhon Doe", playerColor = Green41B.toArgb()),
            rank = 1,
            totalPoint = 200
        ),
        LeaderBoard(
            player = Player(username = "Jhon Doe", playerColor = Green41B.toArgb()),
            rank = 2,
            totalPoint = 200
        ),
        LeaderBoard(
            player = Player(username = "Jhon Doe", playerColor = Green41B.toArgb()),
            rank = 3,
            totalPoint = 200
        ),
    )
    GeoHuntTheme {
        LeaderBoardContent(LeaderBoardUiState(
            leaderBoardPodiumList = leaderBoardList,
            leaderBoardNotPodiumList = leaderBoardList,
            leaderBoardState = LeaderBoardState.Success
        )) { }
    }
}