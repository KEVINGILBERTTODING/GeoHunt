package com.geohunt.presentation.room.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.geohunt.R
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.GreenE6
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.domain.model.Player
import com.geohunt.domain.model.Room
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
                is RoomEffect.onBack -> { onBackPressed() }
            }
        }
    }
    RoomContent(state, context, roomVm.userData.userId,
        {
        roomVm::onIntent
    }, { onBackPressed() })
}

@Composable
fun RoomContent(state: RoomUiState, context: Context,uid: String,
                onIntent: (RoomIntent) -> Unit = {},
                onBackPressed: () -> Unit) {
    val buttonColor = when {
        state.isLoading.not() -> Green41B
        else -> Color.Gray
    }
    val buttonText = when {
        state.isLoading -> stringResource(R.string.loading_game)
        else -> stringResource(R.string.start)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround) {
            IconButton(
                onClick = { onBackPressed() }
            ){
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_arrow),
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier.weight(1f)
                    .fillMaxWidth(),
                text = state.room.info.roomCode,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    color = Black1212,
                    fontWeight = FontWeight.Medium
                ),
            )
            IconButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, state.room.info.roomCode)
                        setPackage("com.whatsapp")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share via"))
                }
            ){
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = ""
                )
            }

        }
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
                val players = state.room.players
                players.forEach { player ->
                    ItemPlayer (uid, player, {})
                    Spacer(Modifier.height(10.dp))
                }
            }
        }

        if (state.isLoading.not()) {
           Box(Modifier.padding(bottom = 8.dp)) {
               CustomButton(
                   buttonColor, 14.sp, Black1212,
                   FontWeight.Medium, White, buttonText, {
                       if (state.isLoading.not()) {

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
        ), LocalContext.current, "", {}, {}
        )
    }
}