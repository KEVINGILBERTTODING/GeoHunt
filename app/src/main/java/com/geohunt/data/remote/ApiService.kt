package com.geohunt.data.remote

import com.geohunt.data.dto.api.kartaview.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("2.0/photo/")
    suspend fun getPhotos(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("zoomLevel") zoom: Int = 15,
        @Query("join") join: String = "sequence",
        @Query("orderBy") orderBy: String = "id",
        @Query("orderDirection") orderDir: String = "desc",
        @Query("radius") radius: Int = 50
    ): Response<ApiResponse>
}