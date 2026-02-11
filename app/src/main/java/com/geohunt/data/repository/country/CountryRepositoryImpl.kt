package com.geohunt.data.repository.country

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.country.Country
import com.geohunt.data.remote.GameService
import com.geohunt.domain.repository.CountryRepository
import timber.log.Timber
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val gameService: GameService
) : CountryRepository {
    override suspend fun getAllCountries(): Resource<List<Country>> {
        return try {
            val response = gameService.getCountries()
            if (response.isSuccessful) {
                return Resource.Success(response.body()!!)
            }else {
                Resource.Error(response.message(), Exception())
            }
        } catch (e: Exception) {
            Timber.d("error country ${e.message}")
            Resource.Error(e.message.toString(), Exception())
        }
    }
}