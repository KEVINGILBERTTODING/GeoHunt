package com.geohunt.data.dto.api.kartaview

data class PhotoResponse(
    val autoImgProcessingStatus: String,
    val imageProcUrl: String,
    val lat: String,
    val lng: String
)