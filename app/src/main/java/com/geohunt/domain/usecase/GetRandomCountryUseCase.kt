package com.geohunt.domain.usecase

import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.country.Country
import javax.inject.Inject
import kotlin.random.Random

class GetRandomCountryUseCase @Inject constructor() {
    operator fun invoke(countries: List<Country>): Country {
       return countries.random()
    }
}