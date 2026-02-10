package com.geohunt.domain.usecase

import com.geohunt.domain.model.country.Country
import javax.inject.Inject
import kotlin.random.Random

class GetRandomLatLngUseCase @Inject constructor() {
    operator fun invoke(country: Country, range: Double = 3.0): Pair<String, String> {
        val lat = country.lat.toDouble() + Random.nextDouble(-range, range)
        val lng = country.lng.toDouble() + Random.nextDouble(-range, range)
        return lat.toString() to lng.toString()
    }
}