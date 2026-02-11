package com.geohunt.domain.usecase

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.country.Country
import com.geohunt.domain.repository.CountryRepository
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(): Resource<List<Country>> {
        return repository.getAllCountries()
    }
}