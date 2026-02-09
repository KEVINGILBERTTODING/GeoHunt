package com.geohunt.core.vm.singlePlayer

import android.util.Log
import androidx.lifecycle.ViewModel
import com.geohunt.domain.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SinglePlayerVm @Inject constructor(): ViewModel() {
    private val _selectedCountry = MutableStateFlow(Country("", mutableListOf()))
    val selectedCountry = _selectedCountry.asStateFlow()
    private val TAG = "SinglePlayerVm"

    fun setSelectedCountry(country: Country) {
        _selectedCountry.value = country
        Log.d(TAG, "bbox: ${randomBboxNearCity(-7.79558, 110.36949)}")
    }

    fun randomBbox(
        parent: List<Double>,
        size: Double = 0.099,
        decimals: Int = 6  // jumlah desimal
    ): List<Double> {

        val minLng = parent[0]
        val minLat = parent[1]
        val maxLng = parent[2]
        val maxLat = parent[3]

        val randomMinLng = minLng + Math.random() * ((maxLng - minLng) - size)
        val randomMinLat = minLat + Math.random() * ((maxLat - minLat) - size)

        val randomMaxLng = randomMinLng + size
        val randomMaxLat = randomMinLat + size

        // round ke "decimals" digit
        fun round(value: Double) = "%.${decimals}f".format(value).toDouble()

        return listOf(
            round(randomMinLng),
            round(randomMinLat),
            round(randomMaxLng),
            round(randomMaxLat)
        )
    }

    val cityCenters = mapOf(
        "Jakarta" to Pair(-6.21462, 106.84513),
        "Bandung" to Pair(-6.92222, 107.60694),
        "Surabaya" to Pair(-7.24917, 112.75208),
        "Medan" to Pair(3.58333, 98.66667),
        "Yogyakarta" to Pair(-7.79558, 110.36949),
        "Bali" to Pair(-8.65, 115.2167),
        "Makassar" to Pair(-5.14861, 119.43194),
        "Semarang" to Pair(-6.991, 110.42083),
        "Denpasar" to Pair(-8.65, 115.2167)
    )


    fun randomBboxNearCity(lat: Double, lng: Double, delta: Double = 0.005): List<Double> {
        val minLng = lng + (Math.random() * 2 - 1) * delta
        val minLat = lat + (Math.random() * 2 - 1) * delta
        val maxLng = minLng + delta
        val maxLat = minLat + delta
        return listOf(minLng, minLat, maxLng, maxLat)
    }


}