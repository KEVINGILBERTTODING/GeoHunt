package com.geohunt.domain.usecase

import com.geohunt.core.resource.Resource
import com.geohunt.domain.model.GamePhoto
import com.geohunt.domain.repository.KartaViewRepository
import javax.inject.Inject

class GetSinglePhotoUseCase @Inject constructor(
    private val repository: KartaViewRepository
){
    suspend operator fun invoke(lat: String, lng: String): Resource<GamePhoto?> {
        val result = repository.getPhoto(lat, lng)
        return when (result) {
            is Resource.Success -> Resource.Success(result.data.randomOrNull())
            is Resource.Loading -> Resource.Loading
            is Resource.Error -> Resource.Error(result.message, result.exception)
            else -> Resource.Error("Unknown error", Exception())
        }
    }
}