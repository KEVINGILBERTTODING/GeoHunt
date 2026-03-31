package com.geohunt.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geohunt.R
import com.geohunt.core.ui.component.CustomTextField
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.GrayE0
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.presentation.home.vm.HomeVm
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameModeBottomSheet(homeVm: HomeVm, onClick: () -> Unit, onDissmiss: () -> Unit) {
    val countryState by homeVm.countries.collectAsStateWithLifecycle()
    LaunchedEffect(countryState) {
        Timber.d("countryState ${countryState.size}")
    }
    ModalBottomSheet(
        onDismissRequest = { onDissmiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = GrayE0
            )
        }
    ) {
        Content(homeVm.gameModeState, {
            homeVm.selectedGameMode = it
            onClick()
        })
    }
}

@Composable
private fun Content(gameModeList: List<String> = emptyList(),
                    onSelected: (String) -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.select_game_mode),
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Start,
                fontFamily = Poppins, fontSize = 16.sp, color = Black1212,
                fontWeight = FontWeight.Medium
            ),
        )
        Spacer(Modifier.height(20.dp))

        gameModeList.forEach { it ->
            CustomTextField(true, "", Color.White,
                14.sp, true, Black1212, Black1212,
                Black1212, 10.sp, it, true, 1,
                onClick = {
                    onSelected(it)
                })
            Spacer(Modifier.height(10.dp))

        }


    }
}

@Preview(showBackground = true)
@Composable
fun GameModeBottomSheetPreview() {
    GeoHuntTheme {
        Content(listOf("Single Player", "Create Room", "Join Room")) {  }
    }
}