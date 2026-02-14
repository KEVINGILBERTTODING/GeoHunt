package com.geohunt.domain.usecase

import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max

class CalculateScoreUseCase @Inject constructor() {
    operator fun invoke(distanceInMeters: Float, radius: Float = 100f): Int {
        val maxScore = 5000f
        val minScore = 10f
        val targetDistance = 20_000f
        val targetScore = 2000f

        val scale = -targetDistance / ln(targetScore / maxScore)

        val rawScore = maxScore * exp(-distanceInMeters / scale)

        return max(rawScore, minScore).toInt()    }
}