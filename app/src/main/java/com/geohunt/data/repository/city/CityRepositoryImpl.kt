package com.geohunt.data.repository.city

import com.geohunt.data.dto.city.City
import com.geohunt.domain.repository.CityRepository
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(): CityRepository {
    override fun getAllCity(): List<City> {
        return listOf(
            City("Jakarta", "-6.193932715082624", "106.84937065233879"),
            City("Surabaya", "-7.2575", "112.7521"),
            City("Bandung", "-6.914744", "107.609810"),
            City("Medan", "3.595196", "98.672226"),
            City("Semarang", "-6.9667", "110.4167"),
            City("Makassar", "-5.147665", "119.432731"),
            City("Palembang", "-2.9761", "104.7754"),
            City("Denpasar", "-8.6500", "115.2167"),
            City("Yogyakarta", "-7.7972", "110.3688"),
            City("Balikpapan", "-1.2670", "116.8283"),
            City("Pontianak", "0.0333", "109.3333"),
            City("Manado", "1.4911", "124.8410"),
            City("Banjarmasin", "-3.3160", "114.5922"),
            City("Bandar Lampung", "-5.4269", "105.2619")
        )
    }

    override fun getCity(name: String): City {
        return getAllCity().first { it.name == name }

    }
}