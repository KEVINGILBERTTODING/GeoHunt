package com.geohunt.domain.usecase

import javax.inject.Inject

class GenerateRoomCode @Inject constructor() {
    operator fun invoke(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6).map { chars.random() }.joinToString("")
    }
}