package com.geohunt.domain.repository

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.app.AppHealthResponseDto

interface AppHealthRepository {
    suspend fun fetchAndSaveAppHealth(): Resource<AppHealthResponseDto?>
    fun getAppHealthCheck(): Resource<AppHealthResponseDto?>
}