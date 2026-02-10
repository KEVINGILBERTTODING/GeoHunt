package com.geohunt.data.mapper

import com.geohunt.data.dto.api.kartaview.PhotoResponse
import com.geohunt.domain.model.GamePhoto

fun PhotoResponse.toDomain(): GamePhoto? {
    if (autoImgProcessingStatus != "FINISHED") return null
    val latDouble = lat.toDoubleOrNull() ?: return null
    val lngDouble = lng.toDoubleOrNull() ?: return null
    return GamePhoto(imageProcUrl, lat, lng)
}