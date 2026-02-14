package com.geohunt.presentation.map.singlePlayer.game.ui
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.geohunt.R
import com.geohunt.core.navigation.Screen
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.component.CustomFab
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.map.singlePlayer.game.event.GameMapSinglePlayerEvent
import com.geohunt.presentation.map.singlePlayer.game.vm.GameMapSinglePlayerVm

@Composable
fun GameMapSinglePlayerScreen(navController: NavController = rememberNavController()) {
    var isPageLoaded by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(Screen.HomeGraph.route)
    }
    val singlePlayerVm: SinglePlayerVm = hiltViewModel(parentEntry)
    val urlImage by singlePlayerVm.imageUrl.collectAsStateWithLifecycle()

    var showMapPicker by remember { mutableStateOf(false) }

    val mapGameSinglePlayerVm: GameMapSinglePlayerVm = hiltViewModel()

    var isSuccessLoadStreetView by remember { mutableStateOf(false) }

    var showBottomSheetBack by remember { mutableStateOf(false) }



    // EVENT
    LaunchedEffect(Unit) {
        mapGameSinglePlayerVm.mapGameEvent.collect { event ->
            when(event) {
                is GameMapSinglePlayerEvent.ShowMapPicker -> {
                    showMapPicker = true
                }
                is GameMapSinglePlayerEvent.HideMapPicker -> {
                    showMapPicker = false
                }
                is GameMapSinglePlayerEvent.ErrorLoadStreetView -> {
                    navController.navigate(Screen.LoadingScreenSinglePlayer.route) {
                        popUpTo(Screen.GameMapSinglePlayerScreen.route) {
                            inclusive = true
                        }
                    }
                }
                is GameMapSinglePlayerEvent.OnBackPressedEvent -> {
                    showBottomSheetBack = true
                }
                is GameMapSinglePlayerEvent.NavigateToGameResult -> {
                    singlePlayerVm.calculateDistanceAndPoint()
                    navController.navigate(Screen.GameResultSinglePlayerScreen.route) {
                        popUpTo(Screen.GameMapSinglePlayerScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    BackHandler {
        mapGameSinglePlayerVm.setOnBackPressedEvent()
    }

    if (showBottomSheetBack) {
        ConfirmationBottomSheet(stringResource(R.string.return_to_home),
            stringResource(R.string.are_you_sure_you_want_to_return_to_home),
            stringResource(R.string.yes_return_to_home),
            stringResource(R.string.cancel),
            {
                showBottomSheetBack = false
            },
            {
                showBottomSheetBack = false
                navController.popBackStack()
            }) {
            showBottomSheetBack = false
        }
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    settings.loadsImagesAutomatically = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            isPageLoaded = true
                        }
                    }

                    // callback from panorama html
                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun onPanoramaLoaded() {
                            isSuccessLoadStreetView = true
                        }

                        @JavascriptInterface
                        fun onPanoramaError(errorMsg: String) {
                            mapGameSinglePlayerVm.errorLoadStreetViewEvent()
                        }
                    }, "AndroidCallback")

                    loadUrl("file:///android_asset/map.html")
                }
            },
            update = { webView ->
                if (isPageLoaded) {
                    webView.evaluateJavascript(
                        "loadPanorama('$urlImage')",
                        null
                    )
                }

            },
            modifier = Modifier.fillMaxSize()
        )

        if (isSuccessLoadStreetView) {
            Box(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 40.dp, end = 20.dp)) {
                CustomFab(painterResource(R.drawable.ic_eye), Black1212,
                    Green41B, Black1212) {
                    mapGameSinglePlayerVm.showBottomSheetMapPickerEvent()
                }
            }
        }

        AnimatedVisibility(
            visible = showMapPicker,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            GameMapPickerScreen(mapGameSinglePlayerVm,{
                mapGameSinglePlayerVm.hideBottomSheetMapPickerEvent()
            }, { latLng ->
                singlePlayerVm.setGuessedLocation(latLng.first, latLng.second)
                mapGameSinglePlayerVm.hideBottomSheetMapPickerEvent()
                mapGameSinglePlayerVm.navigateToGameResult()
            })
        }

    }
}