package com.geohunt.domain.usecase

import com.geohunt.core.resource.Resource
import com.geohunt.domain.model.GamePhoto
import com.geohunt.domain.repository.KartaViewRepository
import javax.inject.Inject

class GetSinglePhotoUseCase @Inject constructor(
    private val repository: KartaViewRepository
){
    suspend operator fun invoke(lat: String, lng: String): Result<GamePhoto?> {
        val result = repository.getPhoto(lat, lng)
        return when (result) {
            is Resource.Success -> Result.success(result.data.randomOrNull())
            is Resource.Error -> Result.failure(Exception(result.message))
            else -> Result.failure(Exception("Unknown error"))
        }
    }
}