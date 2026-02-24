package com.geohunt.domain.model

data class Player(
    val uid: String = "",
    val username: String = "",
    val isReady: Boolean = false,
    val isConnected: Boolean = false,
    val joinedAt: Long = 0
)
