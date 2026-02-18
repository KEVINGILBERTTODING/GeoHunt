package com.geohunt.presentation.splashScreen.event

sealed class SplashEvent {
    object onSuccess: SplashEvent()
    data class onError(val message: String): SplashEvent()
    data class onMaintenance(val message: String): SplashEvent()
    data class onUpdate(val message: String, val isForceUpdate: Boolean,
        val url: String): SplashEvent()
}
