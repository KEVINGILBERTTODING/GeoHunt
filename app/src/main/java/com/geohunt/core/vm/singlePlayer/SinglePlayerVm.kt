package com.geohunt.core.vm.singlePlayer

import android.util.Log
import androidx.lifecycle.ViewModel
import com.geohunt.domain.model.country.Country
import com.geohunt.domain.repository.CountryRepository
import com.geohunt.domain.usecase.GetRandomLatLngUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SinglePlayerVm @Inject constructor(
    private val countryRepository: CountryRepository,
    private val getRandomLatLngUseCase: GetRandomLatLngUseCase
): ViewModel() {
    private val _selectedCountry = MutableStateFlow(countryRepository.getAllCountries().first())
    val selectedCountry = _selectedCountry.asStateFlow()
    val trueLocation = getRandomLatLngUseCase(_selectedCountry.value)
    val guessedLocation = Pair("0", "0")


    fun setSelectedCountry(country: Country) {
        _selectedCountry.value = country
        Timber.d("selectedCountry ${selectedCountry.value}")
        Timber.d("true location ${trueLocation}")
    }


}