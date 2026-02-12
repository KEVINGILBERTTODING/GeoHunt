package com.geohunt.domain.usecase

import com.geohunt.domain.repository.LoadingGameRepository
import javax.inject.Inject

class GetTipsUseCase @Inject constructor(
    private val loadingGameRepository: LoadingGameRepository
) {
    operator fun invoke(): List<String> {
        return loadingGameRepository.getTipsMessage()
    }
}