package com.geohunt.data.repository.country

import com.geohunt.data.dto.country.Country
import com.geohunt.domain.repository.CountryRepository
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor() : CountryRepository {
    val countries = listOf(
        Country("Indonesia - Jakarta", "-6.193932715082624", "106.84937065233879"),
        Country("United States - New York", "40.7128", "-74.0060"),
        Country("Brazil - Sao Paulo", "-23.5505", "-46.6333"),
        Country("Japan - Tokyo", "35.6895", "139.6917"),
        Country("India - Mumbai", "19.0760", "72.8777"),
        Country("United Kingdom - London", "51.5074", "-0.1278"),
        Country("Germany - Berlin", "52.5200", "13.4050"),
        Country("France - Paris", "48.8566", "2.3522"),
        Country("Canada - Toronto", "43.6532", "-79.3832"),
        Country("Australia - Sydney", "-33.8688", "151.2093"),
        Country("Mexico - Mexico City", "19.4326", "-99.1332"),
        Country("South Korea - Seoul", "37.5665", "126.9780"),
        Country("Italy - Rome", "41.9028", "12.4964"),
        Country("Spain - Madrid", "40.4168", "-3.7038"),
        Country("Turkey - Istanbul", "41.0082", "28.9784"),
        Country("Russia - Moscow", "55.7558", "37.6173"),
        Country("Argentina - Buenos Aires", "-34.6037", "-58.3816"),
        Country("South Africa - Cape Town", "-33.9249", "18.4241"),
        Country("Saudi Arabia - Riyadh", "24.7136", "46.6753")
    )
    override fun getAllCountries(): List<Country> {
        return countries
    }

    override fun getCountry(name: String): Country {
        return countries.first { it.name == name }
    }
}