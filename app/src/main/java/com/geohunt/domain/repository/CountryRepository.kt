package com.geohunt.domain.repository

import com.geohunt.data.dto.country.Country

interface CountryRepository {
    fun getAllCountries() : List<Country>
    fun getCountry(name: String) : Country
}
