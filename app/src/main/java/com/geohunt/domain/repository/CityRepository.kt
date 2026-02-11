package com.geohunt.domain.repository

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.city.City

interface CityRepository {
    suspend fun getCities(): Resource<List<City>>
}