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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geohunt.R
import com.geohunt.core.contract.MultiPlayerIntent
import com.geohunt.core.ui.component.ConfirmationBottomSheet
import com.geohunt.core.ui.component.CustomFab
import com.geohunt.core.ui.component.RoundedGlassContainer
import com.geohunt.core.ui.theme.Black1212
import com.geohunt.core.ui.theme.GeoHuntTheme
import com.geohunt.core.ui.theme.Green41B
import com.geohunt.core.ui.theme.White
import com.geohunt.core.vm.multiPlayer.MultiPlayerVm
import com.geohunt.presentation.loadingScreen.multiplayer.ui.LoadingMpScreen
import com.geohunt.presentation.map.mp.game.component.TimeContainer
import com.geohunt.presentation.map.mp.game.contract.GameMapMpEffect
import com.geohunt.presentation.map.mp.game.contract.GameMapMpIntent
import com.geohunt.presentation.map.mp.game.contract.GameMapMpIntent.*
import com.geohunt.presentation.map.mp.game.contract.GameMapState
import com.geohunt.presentation.map.mp.game.vm.GameMapMpVm
import com.geohunt.presentation.waiting.mp.ui.WaitingPlayerScreen

@Composable
fun GameMapMpScreen(
    multiPlayerVm: MultiPlayerVm,
    onBackToHome: () -> Unit,
    onNavigateToResult: () -> Unit,
    vm: GameMapMpVm = hiltViewModel(),
) {
    var isPageLoaded by remember { mutableStateOf(false) }
    var showMapPicker by remember { mutableStateOf(false) }
    var showBottomSheetBack by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val uiState by vm.state.collectAsStateWithLifecycle()
    val mpState by multiPlayerVm.state.collectAsStateWithLifecycle()
    var loadedPhotoUrl by remember { mutableStateOf("") }
    val textButtonBack = if (uiState.isLoadingBack) stringResource(R.string.loading_game)
    else {
        stringResource(R.string.return_to_home)
    }
    val roundData = uiState.roomData.rounds.lastOrNull()

    // EFFECT
    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when(effect) {
                is GameMapMpEffect.OnBack -> {
                    onBackToHome()
                }
                is GameMapMpEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is GameMapMpEffect.OnTimeUp -> {
                    vm.onIntent(OnSubmitAnswer(
                        Pair(roundData?.trueLat ?: "0.0",
                            roundData?.trueLng ?: "0.0")))
                }

                is GameMapMpEffect.OnNavigateToResult -> {
                    onNavigateToResult()
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
            stringResource(R.string.keep_playing),
            textButtonBack,
            true, true,
            {
                showBottomSheetBack = !showBottomSheetBack
            },
            {
                showBottomSheetBack = !showBottomSheetBack
            }) {
            if (uiState.isLoadingBack.not()) {
                vm.onIntent(GameMapMpIntent.OnBackPressed)
            }else {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            }
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
                            if (uiState.isHost) {
                                multiPlayerVm.onIntent(MultiPlayerIntent.OnUpdateRetryState(false))
                            }
                            vm.onIntent(UpdateUserLoadPanorama(true))
                        }

                        @JavascriptInterface
                        fun onPanoramaError(errorMsg: String) {
                            vm.onIntent(UpdateUserLoadPanorama(false))
                            if (uiState.isHost) {
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
                val photoUrl = uiState.roomData.rounds.last().photoUrl
                if (isPageLoaded && photoUrl != loadedPhotoUrl) {
                    loadedPhotoUrl = photoUrl
                    webView.evaluateJavascript(
                        "loadPanorama('${photoUrl}')",
                        null
                    )
                }

            },
            modifier = Modifier.fillMaxSize()
        )



        when(uiState.gameMapMpState) {
            is GameMapState.Ready -> {
                // FAB
                Box(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 40.dp, end = 20.dp)) {
                    CustomFab(painterResource(R.drawable.ic_eye), Black1212,
                        White, Black1212) {
                        showMapPicker = !showMapPicker
                    }
                }
            }
            is GameMapState.WaitingPlayer -> {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    WaitingPlayerScreen()
                }
            }
            is GameMapState.LoadingStreetView -> {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    LoadingMpScreen()
                }
            }
        }

        AnimatedVisibility(
            visible = showMapPicker,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            GameMapMpPickerScreen(
                uiState,
                mpState,
                {
                    vm.onIntent(it)
                },
                onDismiss = {
                    showMapPicker = !showMapPicker
                }
            )
        }

        // app bar
        Row(
            modifier = Modifier.fillMaxWidth()
                .systemBarsPadding()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RoundedGlassContainer(
                icon = R.drawable.ic_arrow,
                {
                    showBottomSheetBack = !showBottomSheetBack
                }
            )
            Spacer(Modifier.weight(1f))
            AnimatedVisibility(
                visible = uiState.gameMapMpState is GameMapState.Ready,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                TimeContainer(uiState.timeLeft)
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GameMapMpPreview() {
    GeoHuntTheme {

    }
}