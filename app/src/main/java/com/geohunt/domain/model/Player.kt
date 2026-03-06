package com.geohunt.domain.model

data class Player(
    val uid: String = "",
    val username: String = "",
    val ready: Boolean = false,
    val online: Boolean = false,
    val joinedAt: Long = 0,
    val loadPanorama : Boolean = false,
    val playerColor : Int = 0
)