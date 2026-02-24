package com.geohunt.presentation.home.component

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.geohunt.R
import com.geohunt.core.navigation.Screen
import com.geohunt.core.resource.Resource
import com.geohunt.core.ui.component.CustomButton
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.presentation.home.event.JoinRoomEvent
import com.geohunt.presentation.home.event.RoomFormEvent
import com.geohunt.presentation.home.vm.CreateRoomVm
import com.geohunt.presentation.home.vm.JoinRoomVm
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinRoomFormBottomSheet(onDissmiss: () -> Unit) {

    val joinRoomVm: JoinRoomVm = hiltViewModel()
    val formState by joinRoomVm.formState.collectAsStateWithLifecycle()
    val uiState by joinRoomVm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val buttonColor = when(uiState) {
        is Resource.Idle -> Green41B
        else -> Color.Gray
    }
    val buttonText = when(uiState) {
        is Resource.Loading -> stringResource(R.string.loading_game)
        else -> stringResource(R.string.join_room)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            uiState is Resource.Idle
        }
    )


    LaunchedEffect(Unit) {
        joinRoomVm.event.collect { event ->
            when(event) {
                is JoinRoomEvent.NavigateToRoom -> {
                    navController.navigate(Screen.RoomScreen.route)
                }
                is JoinRoomEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is JoinRoomEvent.Submit -> {
                    joinRoomVm.submit()
                }
            }
        }
    }


    ModalBottomSheet(
        onDismissRequest = { if (uiState is Resource.Idle) onDissmiss() },
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = GrayE0
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.create_room),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    fontFamily = Poppins, fontSize = 16.sp, color = Black39,
                    fontWeight = FontWeight.Medium
                ),
            )
            Spacer(Modifier.height(20.dp))
            CustomTextField(
                true,
                stringResource(R.string.room_code),
                Color.White,
                14.sp,
                false,
                Black1212,
                Black1212,
                Black1212,
                10.sp,
                formState.roomCode,
                false,
                1,
                {},
                {
                    if (it.length <= 6) joinRoomVm.onRoomCodeChanged(it.uppercase())
                },
                true,
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Characters),
                KeyboardActions.Default
            )

            Spacer(Modifier.height(20.dp))
            CustomButton(
                buttonColor, 14.sp, Black1212,
                FontWeight.Medium, White, buttonText, {
                    if (uiState is Resource.Idle) {
                        joinRoomVm.setEvent(JoinRoomEvent.Submit)
                    }
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JoinRoomFormBottomSheetPreview() {
    GeoHuntTheme {
        JoinRoomFormBottomSheet (rememberNavController(), hiltViewModel())
    }
}