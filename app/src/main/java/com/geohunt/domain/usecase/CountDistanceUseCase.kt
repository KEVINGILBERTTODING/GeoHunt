package com.geohunt.domain.usecase

import android.location.Location
import javax.inject.Inject

class CountDistanceUseCase @Inject constructor() {
    operator fun invoke(trueLocation: Pair<String, String>,
                        guessedLocation: Pair<String, String>): Float {
        val trueLocLat = trueLocation.first.toDouble()
        val trueLng = trueLocation.second.toDouble()
        val guessedLat = guessedLocation.first.toDouble()
        val guessedLng = guessedLocation.second.toDouble()

        val result = FloatArray(1)
        Location.distanceBetween(trueLocLat, trueLng,
            guessedLat, guessedLng, result)

        // in meter
        return result[0]
    }
}