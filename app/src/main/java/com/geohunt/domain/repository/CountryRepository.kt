package com.geohunt.domain.repository

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.country.Country

interface CountryRepository {
    suspend fun getAllCountries() : Resource<List<Country>>
}
