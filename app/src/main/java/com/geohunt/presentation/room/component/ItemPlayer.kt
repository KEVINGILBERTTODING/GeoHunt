package com.geohunt.presentation.room.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.core.extension.onColor
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.GreenE6
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.data.dto.room.RoomPlayersDto
import com.geohunt.domain.model.Player
import timber.log.Timber

@Composable
fun ItemPlayer(uid: String, player: Player, onClick: () -> Unit = {}) {
    Box(Modifier.fillMaxWidth()
        .background(
            color = Black1212,
            shape = RoundedCornerShape(16.dp)))
    {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp, top = 1.dp, end = 1.dp, start = 1.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(
                    color = White,
                    shape = RoundedCornerShape(16.dp))
                .clickable(onClick = {
                    onClick()
                })
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    Modifier
                        .padding(8.dp)
                        .size(35.dp)
                        .background(color = Black1212)
                        .align(Alignment.CenterVertically)
                ) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .padding(bottom = 5.dp, top = 1.dp, end = 1.dp, start = 2.dp)
                            .background(
                                color = Color(player.playerColor))
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = player.username.first().toString().uppercase(),
                            fontSize = 16.sp,
                            color = Black1212.onColor(),
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.width(4.dp))
                Column(Modifier.weight(1f).align(Alignment.CenterVertically)) {
                    Text(
                        text = player.username,
                        fontSize = 14.sp,
                        color = Black1212,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (player.ready) "Ready" else "Not Ready",
                        fontSize = 12.sp,
                        color = if (player.ready) Black1212 else GrayE0,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp),
                    text = if (player.online) "Online" else "Offline",
                    fontSize = 10.sp,
                        color = if (player.online) Green41B else GrayE0,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ItemPlayerPreview() {
    GeoHuntTheme {
        ItemPlayer ( "", Player(username = "test", playerColor = -14575885),
             {})
    }
}