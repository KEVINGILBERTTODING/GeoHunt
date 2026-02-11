package com.geohunt.data.remote

import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.country.Country
import retrofit2.Response
import retrofit2.http.GET

interface GameService {
    @GET("KEVINGILBERTTODING/KEVINGILBERTTODING/refs/heads/master/country.json")
    suspend fun getCountries(): Response<List<Country>>
    @GET("KEVINGILBERTTODING/KEVINGILBERTTODING/refs/heads/master/cities.json")
    suspend fun getCities(): Response<List<City>>
}