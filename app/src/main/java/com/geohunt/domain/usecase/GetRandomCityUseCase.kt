package com.geohunt.domain.usecase

import com.geohunt.data.dto.city.City
import javax.inject.Inject

class GetRandomCityUseCase @Inject constructor() {
    operator fun invoke(countryId: Int, cities: List<City>): City {
        return if (countryId == 1) cities.random() else
            cities.filter { it.country_id == countryId }.random()
    }
}