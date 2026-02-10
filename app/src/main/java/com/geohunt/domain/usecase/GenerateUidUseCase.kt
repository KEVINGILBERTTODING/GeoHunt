package com.geohunt.domain.usecase

import java.util.UUID
import javax.inject.Inject

class GenerateUidUseCase @Inject constructor() {
    operator fun invoke(): String {
        return UUID.randomUUID().toString()
    }
}