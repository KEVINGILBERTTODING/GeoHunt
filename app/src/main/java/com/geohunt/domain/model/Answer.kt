package com.geohunt.domain.model

data class Answer(
    val lat: String = "",
    val lng: String = "",
    val point: Int = 0,
    val distance: Float = 0f
)