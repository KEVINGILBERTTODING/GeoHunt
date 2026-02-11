package com.geohunt.presentation.map.singlePlayer.game.ui
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.geohunt.core.navigation.Screen
import com.geohunt.core.ui.component.MyFab
import com.geohunt.core.vm.singlePlayer.SinglePlayerVm
import com.geohunt.presentation.map.singlePlayer.game.component.GameMapPickerBottomSheet
import com.geohunt.presentation.map.singlePlayer.game.event.GameMapSinglePlayerEvent
import com.geohunt.presentation.map.singlePlayer.game.vm.GameMapSinglePlayerVm
import timber.log.Timber

@Composable
fun GameMapSinglePlayer(navController: NavController = rememberNavController()) {
    var isPageLoaded by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(Screen.HomeGraph.route)
    }
    val singlePlayerVm: SinglePlayerVm = hiltViewModel(parentEntry)
    val urlImage by singlePlayerVm.imageUrl.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }

    val mapGameSinglePlayerVm: GameMapSinglePlayerVm = hiltViewModel()



    // EVENT
    LaunchedEffect(Unit) {
        mapGameSinglePlayerVm.mapGameEvent.collect { event -> {
            when(event) {
                is GameMapSinglePlayerEvent.showGameMapSinglePlayerPickerBottomSheet -> {
                    Timber.d("showGameMapSinglePlayerPickerBottomSheet")
                    showBottomSheet = true
                }
            }
        } }
    }

    if (showBottomSheet){
        GameMapPickerBottomSheet{showBottomSheet = false}
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
        MyFab(modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
            mapGameSinglePlayerVm.showBottomSheetMapPicker()
            showBottomSheet = true
        }
    }
}