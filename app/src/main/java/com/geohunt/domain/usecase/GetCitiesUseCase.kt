package com.geohunt.domain.usecase

import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.city.City
import com.geohunt.domain.repository.CityRepository
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    suspend operator fun invoke(): Resource<List<City>> {
        return cityRepository.getCities()
    }
}