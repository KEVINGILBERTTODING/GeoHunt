package com.geohunt.presentation.room.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geohunt.R
import com.geohunt.core.contract.MultiPlayerEffect
import com.geohunt.core.contract.MultiPlayerIntent.*
import com.geohunt.core.contract.MultiPlayerUiState
import com.geohunt.core.ui.component.AppBar
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.core.vm.multiPlayer.MultiPlayerVm
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Room
import com.geohunt.presentation.loadingScreen.multiplayer.ui.LoadingMpScreen
import com.geohunt.presentation.map.singlePlayer.game.ui.GameMapPickerScreen
import com.geohunt.presentation.room.component.ItemPlayer
import com.geohunt.presentation.room.component.RoomEmpty
import com.geohunt.presentation.room.component.RoomListLoading
import com.geohunt.presentation.room.contract.RoomEffect
import com.geohunt.presentation.room.contract.RoomIntent
import com.geohunt.presentation.room.contract.RoomUiState
import com.geohunt.presentation.room.vm.RoomVm
import timber.log.Timber

@Composable
fun RoomScreen(
    onBackPressed: () -> Unit,
    multiPlayerVm: MultiPlayerVm,
    onNavigateToGame: () -> Unit,
    roomVm: RoomVm = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by roomVm.state.collectAsStateWithLifecycle()
    var showBottomSheetBack by remember { mutableStateOf(false) }
    val textButtonBack = if (state.isLoadingBack) stringResource(R.string.loading_game)
    else {
        stringResource(R.string.return_to_home)
    }

    LaunchedEffect(Unit) {
        multiPlayerVm.effect.collect { effect ->
            when(effect) {
                is MultiPlayerEffect.OnBack -> { onBackPressed() }
                is MultiPlayerEffect.OnSuccess -> {}
                is MultiPlayerEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        roomVm.effect.collect { event ->
            Timber.d("eventroom ${event}")
            when(event) {
                is RoomEffect.NavigateToGame -> { onNavigateToGame() }
                is RoomEffect.ShowToast -> {Toast.makeText(context, event.message,
                    Toast.LENGTH_SHORT).show() }
                is RoomEffect.OnBack -> {
                    Timber.d("remove room")
                    onBackPressed()
                }
                is RoomEffect.StartGame -> {
                    multiPlayerVm.onIntent(OnStartGame(state.room.rounds.size))
                }
            }
        }
    }


    if (showBottomSheetBack) {
        ConfirmationBottomSheet(stringResource(R.string.return_to_home),
            stringResource(R.string.are_you_sure_you_want_to_return_to_home),
            stringResource(R.string.keep_playing),
            textButtonBack,
            true, true,
            {
                showBottomSheetBack = !showBottomSheetBack
            },
            {
                showBottomSheetBack = !showBottomSheetBack
            }) {
            if (state.isLoadingBack.not()) {
                roomVm.onIntent(RoomIntent.OnBack)
            }else {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    BackHandler {
        showBottomSheetBack = !showBottomSheetBack
    }

    Crossfade(
        targetState = state.room.rounds.lastOrNull()?.status ==  "loading",
        animationSpec = tween(300)
    ) { isLoading ->
        if (isLoading) {
            LoadingMpScreen()
        }else {
            if (state.room.rounds.lastOrNull()?.status == "success") {
                onNavigateToGame()
            }
            RoomContent(state, context,
                { roomVm.onIntent(it) },
                {
                    showBottomSheetBack = !showBottomSheetBack
                }
            )
        }
    }
}

@Composable
fun RoomContent(state: RoomUiState, context: Context,
                onIntent: (RoomIntent) -> Unit = {},
                onBackPressed: () -> Unit) {


    val buttonReadyColor =  when {
        state.isReady -> Color.Gray
        else -> Green41B
    }
    val textReadyButton = when {
        state.isLoadingReady -> stringResource(R.string.loading_game)
        state.isReady -> stringResource(R.string.cancel)
        else -> stringResource(R.string.ready)
    }
    val buttonColor = when {
        state.isLoading.not() -> Green41B
        else -> Color.Gray
    }
    val buttonText = when {
        state.isLoading -> stringResource(R.string.loading_game)
        else -> stringResource(R.string.start)
    }


    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)
        .systemBarsPadding()
        .navigationBarsPadding()) {
        AppBar(
            actionIcon = R.drawable.ic_share,
            navigationIcon = R.drawable.ic_arrow,
            title = state.room.info.roomCode,
            navigationIconClick = {
                onBackPressed()
            },
            actionIconClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, state.room.info.roomCode)
                    setPackage("com.whatsapp")
                }
                context.startActivity(Intent.createChooser(intent, "Share via"))
            }
        )
        Spacer(Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "List Player",
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 16.sp,
                color = Black1212,
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(Modifier.height(20.dp))
        if (state.isLoading) {
            Box(Modifier.fillMaxSize()) {
                RoomListLoading()
            }
        }else if(state.room.players.isEmpty()) {
            Box(Modifier.fillMaxSize()) {
                RoomEmpty()
            }
        }else if (state.room.players.isNotEmpty()) {
            Column(Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .padding(bottom = 20.dp)
                .fillMaxWidth()) {
                    state.room.players.forEach { player ->
                    ItemPlayer (state.userData.userId, player, {})
                    Spacer(Modifier.height(10.dp))
                }
            }
        }

        // start game button
        if (state.isHost && state.isLoading.not()) {
           Box(Modifier.padding(bottom = 16.dp)) {
               CustomButton(
                   buttonColor, 14.sp, Black1212,
                   FontWeight.Medium, White, buttonText, {
                       if (state.isLoading.not()) {
                           onIntent(RoomIntent.OnStartGame)
                       }
                   }
               )
           }
        }

        // set ready button
        if (state.isHost.not() && state.isLoading.not()) {
            Box(Modifier.padding(bottom = 16.dp)) {
                CustomButton(
                    buttonReadyColor, 14.sp, Black1212,
                    FontWeight.Medium, White, textReadyButton, {
                        if (state.isLoadingReady.not()) {
                            Timber.d("player ready ${state.isReady.not()}")
                            state.room.players.find { it.uid ==  state.userData.userId}?.let { player ->
                                Timber.d("player ready ${state.isReady.not()}")
                                onIntent(RoomIntent.OnPlayerReady(!state.isReady))
                            }
                        }
                    }
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun RoomScreenPreview() {
    val players = mutableListOf<Player>()
    repeat(10) {
        players.add(Player(username = "test"))
    }
    GeoHuntTheme {
        RoomContent(RoomUiState(isLoading = false, error = null, room = Room(
            players = players)
        ), LocalContext.current,  {},
            {}
        )
    }
}