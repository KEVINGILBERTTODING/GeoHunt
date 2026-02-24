package com.geohunt.presentation.home.component

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.geohunt.core.ui.component.TextContainer
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.GreenE6
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.core.ui.theme.White
import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.country.Country
import com.geohunt.presentation.home.event.RoomFormEvent
import com.geohunt.presentation.home.vm.CreateRoomVm
import com.geohunt.presentation.home.vm.HomeVm
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomBottomSheet(onDissmiss: () -> Unit) {

    val createRoomVm: CreateRoomVm = hiltViewModel()
    val formState by createRoomVm.formState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val uiState by createRoomVm.uiState.collectAsStateWithLifecycle()
    val buttonColor = when(uiState) {
        is Resource.Idle -> Green41B
        else -> Color.Gray
    }
    val buttonText = when(uiState) {
        is Resource.Loading -> stringResource(R.string.loading_game)
        else -> stringResource(R.string.create_room)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            uiState is Resource.Idle
        }
    )


    LaunchedEffect(Unit) {
        createRoomVm.event.collect { event ->
            when(event) {
                RoomFormEvent.NavigateToRoom -> {
                    onDissmiss()

                }
                RoomFormEvent.OnSubmit -> createRoomVm.submit()
                is RoomFormEvent.ShowToastEvent ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
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

            CustomTextField(true, stringResource(R.string.total_round), Color.White,
                14.sp, false, Black1212, Black1212,
                Black1212, 10.sp, formState.totalRounds, false, 1,
                {}, {
                    if (it.length <= 2) createRoomVm.onRoomRoundChange(it)
                }, true,
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), KeyboardActions.Default)

            Spacer(Modifier.height(10.dp))

            CustomTextField(true, stringResource(R.string.duration_per_round), Color.White,
                14.sp, false, Black1212, Black1212,
                Black1212, 10.sp, formState.durationPerRound, false, 1,
                {}, {
                    if (it.length <= 3) createRoomVm.onRoomDurationChange(it)
                }, true,
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), KeyboardActions.Default)
            Spacer(Modifier.height(20.dp))

            CustomButton(
                buttonColor, 14.sp, Black1212,
                FontWeight.Medium, White, buttonText, {
                    if (uiState is Resource.Idle) {
                        createRoomVm.setEvent(RoomFormEvent.OnSubmit)
                    }
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateRoomBottomSheetPreview() {
    GeoHuntTheme {
        CreateRoomBottomSheet(hiltViewModel())
    }
}