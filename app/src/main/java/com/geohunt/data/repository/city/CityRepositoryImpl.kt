package com.geohunt.data.repository.city

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.city.City
import com.geohunt.data.remote.GameService
import com.geohunt.domain.repository.CityRepository
import timber.log.Timber
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val gameService: GameService
): CityRepository {
    override suspend fun getCities(): Resource<List<City>> {
        return try {
            val response = gameService.getCities()
            if (response.isSuccessful) {
                return Resource.Success(response.body()!!)
            }
            Resource.Error(response.message(), Exception())
        } catch (e: Exception) {
            Resource.Error(e.message.toString(), Exception())
        }
    }
}