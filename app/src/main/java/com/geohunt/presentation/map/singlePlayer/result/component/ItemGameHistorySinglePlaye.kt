package com.geohunt.presentation.map.singlePlayer.result.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.navigation.compose.rememberNavController
import com.geohunt.R
import com.geohunt.core.extension.toPrettierDistanceString
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.BlueE6
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Orange
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.domain.model.GameHistorySinglePlayer

@Composable
fun ItemGameHistorySinglePlayer(index: Int, gameHistorySinglePlayer: GameHistorySinglePlayer) {
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
                    text = "Round $index",
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
                            append("+${gameHistorySinglePlayer.point}")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Black1212
                            )
                        ) {
                            append(" . ${gameHistorySinglePlayer.distance.toPrettierDistanceString()}")
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
                        colorFilter = ColorFilter.tint(BlueE6),
                        contentDescription = ""
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "${gameHistorySinglePlayer.trueLocation.first}, ${gameHistorySinglePlayer.trueLocation.second}",
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Black1212,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal,
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    Box(Modifier.width(24.dp)) {
                        Box(Modifier.align(Alignment.Center)) {
                            DashedVerticalLine(
                                color = GrayE0,
                                dashHeight = 8f,
                                gapHeight = 7f,
                                lineHeight = 30.dp,
                                lineWidth = 2.dp
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Box(Modifier.align(Alignment.CenterVertically)) {
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
                Row(Modifier.fillMaxWidth()) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_marker),
                        colorFilter = ColorFilter.tint(Orange),
                        contentDescription = ""
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "${gameHistorySinglePlayer.guessedLocation.first}, ${gameHistorySinglePlayer.guessedLocation.second}",
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Black1212,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal,
                    )
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