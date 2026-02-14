package com.geohunt.domain.model

import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.country.Country

data class GameHistorySinglePlayer(
    val country: Country,
    val city: City,
    val trueLocation: Pair<String, String>,
    val guessedLocation: Pair<String, String>,
    val point: Int,
    val distance: Float,
    val no: Int
)
