package com.geohunt.presentation.leaderboard.component

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.R
import com.geohunt.core.extension.onColor
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.domain.model.LeaderBoard
import com.geohunt.domain.model.Player

@Composable
fun LeaderBoardBar(
    bgColor: Color,
    fontColor: Color,
    width: Dp,
    leaderBoard: LeaderBoard,
    isShowAvatar: Boolean = true
) {
    Column(Modifier.width(width),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isShowAvatar) {
            Avatar(width, leaderBoard)
        }
        Bar(bgColor, fontColor, leaderBoard, width)
    }
}

@Composable
private fun Avatar(width: Dp, leaderBoard: LeaderBoard){
    val avatarSize = when (leaderBoard.rank) {
        1 -> width - 20.dp
        2 -> width - 30.dp
        else -> width - 40.dp
    }

    val density = LocalDensity.current
    val fontSize = with(density) {
        when(leaderBoard.rank) {
            1 -> (avatarSize - 30.dp).toSp()
            2 -> (avatarSize - 20.dp).toSp()
            else -> (avatarSize - 15.dp).toSp()
        }
    }
    Box(
        Modifier
            .padding(8.dp)
            .size(avatarSize)
            .background(color = Black1212)
    ) {
        Box(
            modifier = Modifier
                .size(avatarSize)
                .padding(bottom = 5.dp, top = 1.dp, end = 1.dp, start = 2.dp)
                .background(
                    color = Color(leaderBoard.player.playerColor))
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = leaderBoard.player.username.first().toString().uppercase(),
                fontSize = fontSize,
                color = Black1212.onColor(),
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold
            )
        }
    }}


@Composable
private fun Bar(
    bgColor: Color,
    fontColor: Color,
    leaderBoard: LeaderBoard,
    width: Dp
){
    val fontSize = when(leaderBoard.rank) {
        1 -> 50.sp
        2 -> 40.sp
        else -> 30.sp
    }
    Box() {
        //shadow card
        Card(
            modifier =  Modifier
                .offset(x = 10.dp, y = 6.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = Black1212
            )
        ) {
            Box(
                Modifier
                    .padding(top = 20.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
                    .width(width)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (leaderBoard.rank) {
                        1 -> {
                            Spacer(Modifier.height(50.dp))
                        }
                        2 -> {
                            Spacer(Modifier.height(25.dp))
                        }
                        else -> {}
                    }
                    Text(
                        text = leaderBoard.rank.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = fontSize,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = Black1212,
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = leaderBoard.totalPoint.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = Black1212,
                    )
                    Text(
                        text = stringResource(R.string.points),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = Black1212,
                    )
                }
            }
        }

        // Content
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = bgColor
            ),
            border = BorderStroke(
                width = 3.dp,
                color = Black1212
            )
        ) {
            Box(
                Modifier
                    .padding(top = 20.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
                    .width(width)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (leaderBoard.rank) {
                        1 -> Spacer(Modifier.height(50.dp))
                        2 -> Spacer(Modifier.height(25.dp))
                        else -> {}
                    }
                    Text(
                        text = leaderBoard.rank.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = fontSize,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = fontColor,
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = leaderBoard.totalPoint.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = Green41B,
                    )
                    Text(
                        text = stringResource(R.string.points),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = Black39,
                    )
                }

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LeaderBoardBarPreview() {
    GeoHuntTheme {
        LeaderBoardBar(
            bgColor = White,
            fontColor = Black1212,
            width = 100.dp,
            leaderBoard = LeaderBoard(
                player = Player(username = "Jhon Doesdsdsddsdsdsd", playerColor = Green41B.toArgb()),
                rank = 1,
                totalPoint = 200
            ),

        )
    }
}