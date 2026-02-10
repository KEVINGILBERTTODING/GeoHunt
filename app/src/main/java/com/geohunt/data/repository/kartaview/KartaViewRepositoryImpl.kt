package com.geohunt.data.repository.kartaview

import com.geohunt.core.resource.Resource
import com.geohunt.data.mapper.toDomain
import com.geohunt.data.remote.ApiService
import com.geohunt.domain.model.GamePhoto
import com.geohunt.domain.repository.KartaViewRepository
import javax.inject.Inject
import kotlin.collections.emptyList

class KartaViewRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): KartaViewRepository {
    override suspend fun getPhoto(
        lat: String,
        lng: String
    ): Resource<List<GamePhoto>> {
        return try {
            val response = apiService.getPhotos(lat.toDouble(), lng.toDouble())
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status.httpCode == 200 && body.result.data.isNotEmpty()) {
                    val photos = body.result.data.mapNotNull { it.toDomain() }
                    Resource.Success(photos)
                }else {
                    Resource.Success(emptyList())
                }
            }else {
                Resource.Error(response.body()?.status?.apiMessage, Exception())
            }
        }catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error", e)
        }
    }
}