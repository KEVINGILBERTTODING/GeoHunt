package com.geohunt.presentation.map.singlePlayer.game

import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.io.File

@Composable
fun GameMapSinglePlayer(navController: NavController = rememberNavController()) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            settings.allowFileAccess = true

            // Enable hardware acceleration
            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            loadUrl("https://cdn.kartaview.org/pr:sharp/aHR0cHM6Ly9zdG9yYWdlMTMub3BlbnN0cmVldGNhbS5vcmcvZmlsZXMvcGhvdG8vMjAyMi8xMC82L3dyYXBwZWRfcHJvYy82MTg3NjA5XzYyNmI4XzYzM2Y1ZDM5MmI0YmUuanBn")
        }
    },
        modifier = Modifier.fillMaxSize()
    )
}