package com.geohunt.presentation.home.vm

import androidx.lifecycle.ViewModel
import com.geohunt.domain.model.Country

class HomeVm: ViewModel() {
    val countries = listOf(
        Country("Indonesia", listOf(95.0, -11.0, 141.0, 6.0)),
//        Country("World", listOf(-180.0, -85.0, 180.0, 85.0)),
//        Country("United States", listOf(-125.0, 24.0, -66.0, 49.0)),
//        Country("Japan", listOf(129.0, 31.0, 146.0, 45.0)),
//        Country("United Kingdom", listOf(-8.0, 49.0, 2.0, 61.0)),
//        Country("France", listOf(-5.0, 41.0, 9.5, 51.0)),
//        Country("Germany", listOf(5.5, 47.0, 15.5, 55.0)),
//        Country("Brazil", listOf(-74.0, -34.0, -34.0, 5.0)),
//        Country("India", listOf(68.0, 6.0, 97.0, 36.0)),
//        Country("Australia", listOf(112.0, -44.0, 154.0, -10.0)),
//        Country("Canada", listOf(-141.0, 42.0, -52.0, 83.0)),
//        Country("South Korea", listOf(125.0, 33.0, 130.0, 39.0)),
//        Country("Italy", listOf(6.0, 36.0, 19.0, 47.0))

    )
}