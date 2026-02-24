package com.geohunt.presentation.room.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GreenE6
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Room
import com.geohunt.presentation.home.component.ItemCountry
import com.geohunt.presentation.home.ui.HomeScreen
import com.geohunt.presentation.room.component.ItemPlayer
import com.geohunt.presentation.room.component.RoomEmpty
import com.geohunt.presentation.room.component.RoomListLoading
import com.geohunt.presentation.room.contract.RoomEffect
import com.geohunt.presentation.room.contract.RoomIntent
import com.geohunt.presentation.room.contract.RoomUiState
import com.geohunt.presentation.room.vm.RoomVm

@Composable
fun RoomScreen(
    onBackPressed: () -> Unit,
    onNavigateToGame: () -> Unit,
    roomVm: RoomVm = hiltViewModel()
) {
    val context = LocalContext.current
    val state by roomVm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        roomVm.effect.collect { event ->
            when(event) {
                is RoomEffect.NavigateToGame -> { onNavigateToGame() }
                is RoomEffect.ShowToast -> Toast.makeText(context, event.message,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    RoomContent(state, {
        roomVm::onIntent
    })
}

@Composable
fun RoomContent(state: RoomUiState, onIntent: (RoomIntent) -> Unit = {}) {
    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Room")
        Spacer(Modifier.height(20.dp))
        val players = state.room.players.orEmpty()
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize()) {
                    RoomListLoading()
                }
            }
            state.error != null -> {

            }
            state.room.players.isEmpty() -> {
                Box(Modifier.fillMaxSize()) {
                    RoomEmpty()
                }
            }
            players.isNotEmpty() -> {
                players.forEach { player ->
                    ItemPlayer (Color.White, 16.sp, Black1212,
                        FontWeight.Normal, Black1212, player,
                        TextAlign.Start, {
                        })
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomScreenPreview() {
    GeoHuntTheme {
        RoomContent(RoomUiState(isLoading = false, error = null, room = Room(players =
        listOf(
            Player(username = "test"),
            Player(username = "test"),
            Player(username = "test"),
        )))
        )
    }
}