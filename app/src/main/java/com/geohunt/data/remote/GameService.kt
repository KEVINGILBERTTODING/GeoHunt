package com.geohunt.data.remote

import com.geohunt.data.dto.app.AppHealthResponseDto
import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.common.ResponseApiDto
import com.geohunt.data.dto.country.Country
import retrofit2.Response
import retrofit2.http.GET

interface GameService {
    @GET("KEVINGILBERTTODING/GeoHunt/refs/heads/dev/app/src/main/assets/country.json")
    suspend fun getCountries(): Response<List<Country>>
    @GET("KEVINGILBERTTODING/GeoHunt/refs/heads/dev/app/src/main/assets/cities.json")
    suspend fun getCities(): Response<List<City>>
    @GET("KEVINGILBERTTODING/GeoHunt/blob/v1.1/app/src/main/assets/health_check.json")
    suspend fun getAppHealthCheck(): Response<ResponseApiDto<AppHealthResponseDto>>
}