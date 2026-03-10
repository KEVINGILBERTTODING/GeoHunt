package com.geohunt.presentation.map.mp.result.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.R
import com.geohunt.core.extension.toPrettierDistanceString
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.domain.model.Answer
import com.geohunt.domain.model.Room

@Composable
fun ItemGameHistoryMp(answers: Answer, roomData: Room) {
    val round = roomData.rounds.lastOrNull()
    val player = roomData.players.find { it.uid == answers.uid }
    val lat = answers.lat.toDoubleOrNull()?.let { "%.6f".format(it) } ?: "0.0"
    val lng = answers.lng.toDoubleOrNull()?.let { "%.6f".format(it) } ?: "0.0"

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
                    text = player?.username ?: "-",
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    color = Black1212,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(Modifier.width(20.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Green41B
                            )
                        ) {
                            append("+${answers.point}")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Black1212
                            )
                        ) {
                            append(" . ${answers.distance.toPrettierDistanceString()}")
                        }
                    },
                    fontFamily = Poppins,
                    color = Green41B,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(Modifier.height(10.dp))

            Box(Modifier.fillMaxWidth()
                .background(
                    color = GrayE0
                )
                .height(1.dp)
            )
            Spacer(Modifier.height(10.dp))

            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth()) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_marker),
                        colorFilter = ColorFilter.tint(Color(player?.playerColor ?: 0)),
                        contentDescription = ""
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "$lat, $lng",
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Black1212,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal,
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    Spacer(Modifier.width(34.dp))
                    Box(Modifier.align(Alignment.CenterVertically)
                        .clickable(onClick = {

                        })) {
                        Text(
                            text = stringResource(R.string.show_map),
                            fontSize = 12.sp,
                            fontFamily = Poppins,
                            color = Green41B,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ItemGameHistorySinglePreview() {
    GeoHuntTheme {

    }
}