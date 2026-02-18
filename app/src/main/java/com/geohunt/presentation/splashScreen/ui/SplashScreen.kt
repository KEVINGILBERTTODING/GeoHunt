package com.geohunt.presentation.splashScreen.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.geohunt.R
import com.geohunt.core.navigation.Screen
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.Poppins
import com.geohunt.presentation.splashScreen.event.SplashEvent
import com.geohunt.presentation.splashScreen.vm.SplashVm
import kotlinx.coroutines.delay
import androidx.core.net.toUri

@Composable
fun SplashScreen(navController: NavController = rememberNavController()) {
    val viewmodel: SplashVm = hiltViewModel()
    val context = LocalContext.current
    val showBottomSheetMaintenance = remember { mutableStateOf(false) }
    val showBottomSheetUpdate = remember { mutableStateOf(false) }
    val maintenanceMsg = remember { mutableStateOf("") }
    val updateMsg = remember { mutableStateOf("") }
    val isForceUpdate = remember { mutableStateOf(false) }
    val updateUrl = remember { mutableStateOf("") }

    // EVENT
    LaunchedEffect(Unit) {
        viewmodel.event.collect { event ->
            when(event) {
                is SplashEvent.onError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is SplashEvent.onMaintenance -> {
                    maintenanceMsg.value = event.message
                    showBottomSheetMaintenance.value = true
                }
                is SplashEvent.onSuccess -> {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
                is SplashEvent.onUpdate -> {
                    isForceUpdate.value = event.isForceUpdate
                    updateMsg.value = event.message
                    updateUrl.value = event.url
                    showBottomSheetUpdate.value = true
                }
            }
        }
    }


    if (showBottomSheetMaintenance.value) {
        ConfirmationBottomSheet(stringResource(R.string.maintenance),
            maintenanceMsg.value,
            stringResource(R.string.ok),
            "",
            false, false,
            {
                showBottomSheetMaintenance.value = false
            },
            {
                val activity = context as? Activity
                activity?.finish()
            }) {
        }
    }

    if (showBottomSheetUpdate.value) {
        ConfirmationBottomSheet(stringResource(R.string.maintenance),
            updateMsg.value,
            stringResource(R.string.update_now),
            stringResource(R.string.skip),
            isForceUpdate.value.not(), false,
            {
                showBottomSheetUpdate.value = false
            },
            {
                val intent = Intent(Intent.ACTION_VIEW, )
                context.startActivity(intent)
                val activity = context as? Activity
                activity?.finish()
            }) {
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(textAlign = TextAlign.Center),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Green41B
                )) {
                    append("Geo")
                }

                withStyle(style = SpanStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Black1212
                )) {
                    append("Hunt")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    GeoHuntTheme {
        SplashScreen()
    }
}