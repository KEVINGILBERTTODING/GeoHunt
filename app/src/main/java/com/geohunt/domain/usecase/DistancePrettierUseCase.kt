package com.geohunt.domain.usecase

import javax.inject.Inject

class DistancePrettierUseCase @Inject constructor() {
    operator fun invoke(distanceInMeters: Float): String {
        return if (distanceInMeters < 1000) {
            "${distanceInMeters.toInt()} m"
        } else {
            "%.2f km".format(distanceInMeters / 1000)
        }
    }
}