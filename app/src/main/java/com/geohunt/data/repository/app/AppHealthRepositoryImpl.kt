package com.geohunt.data.repository.app

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.app.AppHealthResponseDto
import com.geohunt.data.remote.GameService
import com.geohunt.domain.repository.AppHealthRepository
import javax.inject.Inject

class AppHealthRepositoryImpl @Inject constructor(
    private val gameService: GameService
): AppHealthRepository {
    private var _cachedAppHealth: AppHealthResponseDto? = null

    override suspend fun fetchAndSaveAppHealth(): Resource<AppHealthResponseDto?> {
        return try {
            val response = gameService.getAppHealthCheck()
            if (response.isSuccessful && response.body() != null) {
                _cachedAppHealth = response.body()!!.data
                Resource.Success(_cachedAppHealth)
            } else {
                Resource.Error(response.message(), Exception())
            }
        }catch (e: Exception) {
            Resource.Error(e.message, Exception())
        }
    }

    override fun getAppHealthCheck(): Resource<AppHealthResponseDto?> {
       return if (_cachedAppHealth != null) Resource.Success(_cachedAppHealth)
       else Resource.Error("",
           Exception())
    }

}