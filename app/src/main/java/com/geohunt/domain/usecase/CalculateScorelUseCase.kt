package com.geohunt.domain.usecase

import javax.inject.Inject

class CalculateScorelUseCase @Inject constructor() {
    operator fun invoke(distanceInMeters: Float, radius: Float = 100f): Int {
        val score = 100 - ((distanceInMeters / radius) * 100)
        return score.coerceIn(0f, 100f).toInt()
    }
}