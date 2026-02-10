package com.geohunt.data.repository.country

import com.geohunt.domain.model.country.Country
import com.geohunt.domain.repository.CountryRepository
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor() : CountryRepository {
    val countries = listOf(
        Country("World", "0", "0"),
        Country("Indonesia", "-2.5489", "118.0149"),
        Country("United States", "37.0902", "-95.7129"),
        Country("Brazil", "-14.2350", "-51.9253"),
        Country("Japan", "36.2048", "138.2529"),
        Country("India", "20.5937", "78.9629"),
        Country("United Kingdom", "55.3781", "-3.4360"),
        Country("Germany", "51.1657", "10.4515"),
        Country("France", "46.2276", "2.2137"),
        Country("Canada", "56.1304", "-106.3468"),
        Country("Australia", "-25.2744", "133.7751"),
        Country("Mexico", "23.6345", "-102.5528"),
        Country("South Korea", "35.9078", "127.7669"),
        Country("Italy", "41.8719", "12.5674"),
        Country("Spain", "40.4637", "-3.7492"),
        Country("Turkey", "38.9637", "35.2433"),
        Country("Russia", "61.5240", "105.3188"),
        Country("Argentina", "-38.4161", "-63.6167"),
        Country("South Africa", "-30.5595", "22.9375"),
        Country("Saudi Arabia", "23.8859", "45.0792")
    )
    override fun getAllCountries(): List<Country> {
        return countries
    }

    override fun getCountry(name: String): Country {
        return countries.first { it.name == name }
    }
}