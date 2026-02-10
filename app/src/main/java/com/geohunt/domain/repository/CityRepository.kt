package com.geohunt.domain.repository

import com.geohunt.data.dto.city.City

interface CityRepository {
    fun getAllCity(): List<City>
    fun getCity(name: String): City
}