package com.geohunt.domain.usecase

import com.geohunt.data.dto.city.City
import javax.inject.Inject
import kotlin.random.Random

class GetRandomCityLatLngUseCase @Inject constructor() {
    operator fun invoke(city: City, range: Double = 0.005): Pair<String, String> {
        // ±0.001–0.005 derajat ≈ 100–500 m dari pusat kota
        if (city.lat.isBlank() || city.lng.isBlank()) return "" to ""
        val lat = city.lat.toDouble() + Random.nextDouble(-range, range)
        val lng = city.lng.toDouble() + Random.nextDouble(-range, range)
        return lat.toString() to lng.toString()
    }
}