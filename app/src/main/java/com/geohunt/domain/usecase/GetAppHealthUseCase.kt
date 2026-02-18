package com.geohunt.domain.usecase

import com.geohunt.domain.repository.AppHealthRepository
import javax.inject.Inject

class GetAppHealthUseCase @Inject constructor(
    private val appHealthRepository: AppHealthRepository
) {
    suspend fun getAppHealth() = appHealthRepository.fetchAndSaveAppHealth()
    fun getAppHealthCache() = appHealthRepository.getAppHealthCheck()
}