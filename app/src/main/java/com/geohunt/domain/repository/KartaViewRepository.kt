package com.geohunt.domain.repository

import com.geohunt.core.resource.Resource
import com.geohunt.domain.model.GamePhoto

interface KartaViewRepository {
    suspend fun getPhoto(lat: String, lng: String): Resource<List<GamePhoto>>
}