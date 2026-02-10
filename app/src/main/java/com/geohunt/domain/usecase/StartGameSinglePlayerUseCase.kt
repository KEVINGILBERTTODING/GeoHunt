package com.geohunt.domain.usecase

import com.geohunt.data.dto.city.City
import javax.inject.Inject

class StartGameSinglePlayerUseCase @Inject constructor() {
    operator fun invoke(username: String, uid: String,
                        city: City, trueLocation: Pair<String, String>): Boolean {
        if (username.isBlank()) return false
        if (city.name.isBlank()) return false
        if (city.lng.isEmpty()) return false
        if (city.lat.isEmpty()) return false
        if (trueLocation.first.isBlank()) return false
        if (trueLocation.second.isBlank()) return false
        return true
    }
}