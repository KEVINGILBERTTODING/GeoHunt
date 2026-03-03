package com.geohunt.presentation.map.mp.game.ui
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.geohunt.R
import com.geohunt.core.contract.MultiPlayerIntent
import com.geohunt.core.navigation.Screen
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.component.CustomFab
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.vm.multiPlayer.MultiPlayerVm
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.home.component.CountryBottomSheet
import com.geohunt.presentation.loadingScreen.multiplayer.ui.LoadingMpScreen
import com.geohunt.presentation.map.mp.game.contract.GameMapMpEffect
import com.geohunt.presentation.map.mp.game.contract.GameMapMpIntent
import com.geohunt.presentation.map.mp.game.vm.GameMapMpVm
import com.geohunt.presentation.map.singlePlayer.game.event.GameMapSinglePlayerEvent
import com.geohunt.presentation.map.singlePlayer.game.vm.GameMapSinglePlayerVm

@Composable
fun GameMapMpScreen(
    multiPlayerVm: MultiPlayerVm,
    onBackPressed: () -> Unit,
    vm: GameMapMpVm = hiltViewModel()
) {
    var isPageLoaded by remember { mutableStateOf(false) }
    var showMapPicker by remember { mutableStateOf(false) }
    var isSuccessLoadStreetView by remember { mutableStateOf(false) }
    var showBottomSheetBack by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val uiState by vm.state.collectAsStateWithLifecycle()
    val mpState by multiPlayerVm.state.collectAsStateWithLifecycle()
    val isHostid = uiState.roomData.info.hostId == vm.userData.userId


    // EFFECT
    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when(effect) {
                is GameMapMpEffect.OnBack -> {
                    onBackPressed()
                }
                is GameMapMpEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BackHandler {
        showBottomSheetBack = !showBottomSheetBack
    }

    if (showBottomSheetBack) {
        ConfirmationBottomSheet(stringResource(R.string.return_to_home),
            stringResource(R.string.are_you_sure_you_want_to_return_to_home),
            stringResource(R.string.yes_return_to_home),
            stringResource(R.string.cancel),
            true, true,
            {
                showBottomSheetBack = !showBottomSheetBack
            },
            {
                showBottomSheetBack = !showBottomSheetBack
                onBackPressed()
            }) {
            showBottomSheetBack = !showBottomSheetBack
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
                            multiPlayerVm.onIntent(MultiPlayerIntent.OnUpdateRetryState(false))
                            vm.onIntent(GameMapMpIntent.UpdateUserLoadPanorama(true))
                        }

                        @JavascriptInterface
                        fun onPanoramaError(errorMsg: String) {
                            if (isHostid) {
                                multiPlayerVm.onIntent(MultiPlayerIntent.OnStartGame(
                                    mpState.currentRound)
                                )
                            }
                        }
                    }, "AndroidCallback")

                    loadUrl("file:///android_asset/map.html")
                }
            },
            update = { webView ->
                if (isPageLoaded) {
                    webView.evaluateJavascript(
                        "loadPanorama('${uiState.roomData.rounds.last().photoUrl}')",
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
                    showMapPicker = !showMapPicker
                }
            }
        }

        // loading screen
        AnimatedVisibility(
            visible = uiState.roomData.rounds.last().status == "loading",
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            LoadingMpScreen()
        }

//        AnimatedVisibility(
//            visible = showMapPicker,
//            enter = slideInVertically { it } + fadeIn(),
//            exit = slideOutVertically { it } + fadeOut()
//        ) {
//            GameMapPickerScreen(mapGameSinglePlayerVm,{
//                mapGameSinglePlayerVm.hideBottomSheetMapPickerEvent()
//            }, { latLng ->
//                singlePlayerVm.setGuessedLocation(latLng.first, latLng.second)
//                mapGameSinglePlayerVm.hideBottomSheetMapPickerEvent()
//                mapGameSinglePlayerVm.navigateToGameResult()
//            })
//        }

    }


}

@Preview(showBackground = true)
@Composable
fun GameMapMpPreview() {
    GeoHuntTheme {

    }
}