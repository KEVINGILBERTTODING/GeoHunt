package com.geohunt.domain.usecase

import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.random.Random

class CalculatePointUseCase @Inject constructor() {
    operator fun invoke(distanceInMeters: Float, radius: Float = 100f): Int {
        val maxScore = 5000
        val baseDistance = 2000f // "Zona Nyaman": Jarak 2km skor masih terasa besar

        // 1. Hitung Skor Dasar (Curve Reduction)
        // Rumus: MaxScore * (Base / (Base + Distance))
        val baseScore = maxScore * (baseDistance / (baseDistance + distanceInMeters))

        // 2. Tambahkan Noise/Randomness (Agar tidak membosankan)
        // Memberikan variasi +/- 2% dari skor dasar
        val noise = if (baseScore > 100) Random.nextInt(-50, 50) else 0

        // 3. Bonus Presisi (Jika di bawah radius tertentu)
        val precisionBonus = if (distanceInMeters <= radius) {
            (maxScore * 0.1f).toInt() // Bonus 10% dari skor maksimal
        } else 0

        val finalScore = baseScore.toInt() + noise + precisionBonus

        // 4. Batas Bawah Dinamis
        // Skor minimal adalah 1, bukan 10, agar tetap terasa ada progres
        return finalScore.coerceIn(1, maxScore)
    }
}