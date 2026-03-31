package com.geohunt.presentation.leaderboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.domain.model.LeaderBoard
import com.geohunt.domain.model.Player

@Composable
fun LeaderBoardPodium(leaderBoardList: List<LeaderBoard>) {
    val rank1 = leaderBoardList.filter { it.rank == 1 }
    val rank2 = leaderBoardList.filter { it.rank == 2 }
    val rank3 = leaderBoardList.filter { it.rank == 3 }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (!rank2.isEmpty()) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LeaderBoardBar(
                            bgColor = White,
                            fontColor = Black1212,
                            width = 80.dp,
                            leaderBoard = rank2.first(),
                            isShowAvatar = rank2.count() < 2)
                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (!rank1.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LeaderBoardBar(
                            bgColor = White,
                            fontColor = Black1212,
                            width = 80.dp,
                            leaderBoard = rank1.first(),
                            isShowAvatar = rank1.count() < 2)

                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (!rank3.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LeaderBoardBar(
                            bgColor = White,
                            fontColor = Black1212,
                            width = 80.dp,
                            leaderBoard = rank3.first(),
                            isShowAvatar = rank3.count() < 2)

                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (!rank2.isEmpty()) {
                    val username = if (rank2.count() > 1) {
                        rank2.joinToString(", ") { it.player.username }
                    } else {
                        rank2.first().player.username
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = username,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = Black1212,
                    )
                }
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (!rank1.isEmpty()) {
                    val username = if (rank1.count() > 1) {
                        rank1.joinToString(", ") { it.player.username }
                    } else {
                        rank1.first().player.username
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = username,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = Black1212,
                    )
                }
            }


            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (!rank3.isEmpty()) {
                    val username = if (rank3.count() > 1) {
                        rank3.joinToString(", ") { it.player.username }
                    } else {
                        rank3.first().player.username
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = username,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = Black1212,
                    )
                }
            }
        }
    }


}



@Preview(showBackground = true)
@Composable
fun LeaderBoardPodiumScreenPreview() {
    val leaderBoardList = listOf(
        LeaderBoard(
            player = Player(username = "kevin", playerColor = Green41B.toArgb()),
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
            rank = 2,
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
        )
    )
    GeoHuntTheme {
        LeaderBoardPodium(leaderBoardList)
    }
}