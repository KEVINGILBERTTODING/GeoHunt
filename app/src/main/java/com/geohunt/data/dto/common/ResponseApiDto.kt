package com.geohunt.data.dto.common

data class ResponseApiDto<out T>(
    val status: String,
    val data: T
)
