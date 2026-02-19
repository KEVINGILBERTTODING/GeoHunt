package com.geohunt.core.ui.component

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
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geohunt.R
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.GreenE6
import com.geohunt.core.ui.theme.Poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationBottomSheet(title: String, message: String,
                            titlePositiveBtn: String, titleNegativeBtn: String,
                            showNegativeBtn: Boolean = true, isCanDissmiss: Boolean = true,
                            onDissmiss: () -> Unit, onPositiveClick: () -> Unit,
                             onNegativeClick: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = { onDissmiss() },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { intentValue ->
                    if (!isCanDissmiss) {
                        intentValue != SheetValue.Hidden
                    } else {
                        true
                    }
                }
            ),
        containerColor = Color.White,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = GrayE0
            )
        }
    ) {
        Column(
            Modifier.padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    fontFamily = Poppins, fontSize = 16.sp, color = Black1212,
                    fontWeight = FontWeight.Medium
                ),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = message,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    fontFamily = Poppins, fontSize = 12.sp, color = Black39,
                    fontWeight = FontWeight.Normal
                ),
            )

            Spacer(Modifier.height(25.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (showNegativeBtn) {
                    Box(Modifier.weight(1f)) {
                        CustomButton(
                            Color.White, 12.sp, Black1212,
                            FontWeight.Medium, Black1212, titleNegativeBtn, {
                                onNegativeClick()
                            })
                    }

                }
                Box(Modifier.weight(1f)) {
                    CustomButton(
                        Green41B, 12.sp, Black1212,
                        FontWeight.Medium, Color.White, titlePositiveBtn, {
                            onPositiveClick()
                        })
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ConfirmationBottomSheetPreview() {
    GeoHuntTheme {
        ConfirmationBottomSheet(stringResource(R.string.confirm_exit),
            stringResource(R.string.are_you_sure_you_want_to_exit_the_game),
            stringResource(R.string.keep_playing),
            stringResource(R.string.yes_exit),
            true,
            true,
            {},
            {}) { }
    }
}