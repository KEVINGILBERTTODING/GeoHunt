package com.geohunt.domain.usecase

import com.geohunt.domain.model.country.Country
import javax.inject.Inject

class StartGameSinglePlayerUseCase @Inject constructor() {
    operator fun invoke(username: String, uid: String,
                        country: Country, trueLocation: Pair<String, String>): Boolean {
        if (username.isBlank()) return false
        if (country.name.isBlank()) return false
        if (country.lng.isEmpty()) return false
        if (country.lat.isEmpty()) return false
        if (trueLocation.first.isBlank()) return false
        if (trueLocation.second.isBlank()) return false
        return true
    }
}