package com.geohunt.presentation.home.component

import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geohunt.R
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Black39
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.domain.model.country.Country
import com.geohunt.presentation.home.vm.HomeVm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryBottomSheet(onClick: (Country) -> Unit = {}, onDissmiss: () -> Unit) {
    val homeVm: HomeVm = hiltViewModel()
    ModalBottomSheet(
        onDismissRequest = { onDissmiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Green41B
            )
        }
    ) {
        Column(
            Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.pick_a_country),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    fontFamily = Poppins, fontSize = 16.sp, color = Black39,
                    fontWeight = FontWeight.Medium
                ),
            )
            Spacer(Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(homeVm.getAllCountry()) { country ->
                    ItemCountry(Color.White, 14.sp, Black39,
                        FontWeight.Normal, Black1212, country.name,
                        TextAlign.Start, {
                            onClick(country)
                            onDissmiss()
                        })
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountryBottomSheetPreview() {
    GeoHuntTheme {
        CountryBottomSheet() { }
    }
}