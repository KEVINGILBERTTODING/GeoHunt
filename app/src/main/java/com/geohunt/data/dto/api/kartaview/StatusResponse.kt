package com.geohunt.data.dto.api.kartaview

data class StatusResponse(
    val apiCode: Int,
    val apiMessage: String,
    val httpCode: Int,
    val httpMessage: String
)