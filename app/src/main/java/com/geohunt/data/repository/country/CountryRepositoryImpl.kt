package com.geohunt.data.repository.country

import com.geohunt.data.dto.country.Country
import com.geohunt.domain.repository.CountryRepository
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor() : CountryRepository {
    val countries = listOf(

    )
    override fun getAllCountries(): List<Country> {
        return countries
    }

    override fun getCountry(name: String): Country {
        return countries.first { it.name == name }
    }
}