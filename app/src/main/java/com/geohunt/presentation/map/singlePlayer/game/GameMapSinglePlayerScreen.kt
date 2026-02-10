package com.geohunt.presentation.map.singlePlayer.game
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
@Composable
fun GameMapSinglePlayer(navController: NavController = rememberNavController()) {
    var isPageLoaded by remember { mutableStateOf(false) }
    val imageUrl = "https://cdn.kartaview.org/pr:sharp/aHR0cHM6Ly9zdG9yYWdlMTMub3BlbnN0cmVldGNhbS5vcmcvZmlsZXMvcGhvdG8vMjAyMi8xMC82L3dyYXBwZWRfcHJvYy82MTg3NjA5XzYyNmI4XzYzM2Y1ZDM5MmI0YmUuanBn"
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
                    "loadPanorama('$imageUrl')",
                    null
                )
            }

        },
        modifier = Modifier.fillMaxSize()
    )
}