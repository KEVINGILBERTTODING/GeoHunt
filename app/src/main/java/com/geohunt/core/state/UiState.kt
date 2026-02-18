package com.geohunt.core.state

data class UiState<out T>(
    val isLoading: Boolean = false,
    val idle: Boolean = false,
    val error: String? = null,
    val data: T? = null
)
