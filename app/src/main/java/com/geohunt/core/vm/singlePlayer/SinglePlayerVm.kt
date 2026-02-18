package com.geohunt.core.vm.singlePlayer

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.R
import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.country.Country
import com.geohunt.domain.model.GameHistorySinglePlayer
import com.geohunt.domain.usecase.CalculatePointUseCase
import com.geohunt.domain.usecase.CountDistanceUseCase
import com.geohunt.domain.usecase.GetRandomCityLatLngUseCase
import com.geohunt.domain.usecase.GetRandomCityUseCase
import com.geohunt.domain.usecase.GetSinglePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SinglePlayerVm @Inject constructor(
    private val getRandomCityLatLngUseCase: GetRandomCityLatLngUseCase,
    private val getSinglePhotoUseCase: GetSinglePhotoUseCase,
    private val countDistanceUseCase: CountDistanceUseCase,
    private val calculatePointUseCase: CalculatePointUseCase,
    private val getRandomCityUseCase: GetRandomCityUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {
    val selectedCountry = MutableStateFlow(Country(0, "Random", "", ""))
    private val _trueLocation = MutableStateFlow(Pair("", ""))
    val trueLocation = _trueLocation.asStateFlow()

    private val _guessedLocation = MutableStateFlow(Pair("", ""))
    val guessedLocation = _guessedLocation.asStateFlow()

    private val _selectedCity = MutableStateFlow(
        City(0,0, "","", ""))
    val selectedCity = _selectedCity.asStateFlow()

    private val _loadingState = MutableSharedFlow<Resource<Unit>>()
    val loadingState = _loadingState.asSharedFlow()

    private val _imageUrl = MutableStateFlow("")
    val imageUrl = _imageUrl.asStateFlow()

    var reloadTime = 1 // max 5
    val gameHistory = MutableStateFlow<MutableList<GameHistorySinglePlayer>>(mutableListOf())
    var cityList = mutableListOf<City>()


    fun getPhotos() {
        viewModelScope.launch {
            setLoadingState(Resource.Loading)
            // looping reload time
            while (reloadTime <= 5) {
                if (trueLocation.value.first.isBlank() || trueLocation.value.second.isBlank()) {
                    getNewLatLng()
                }
                val response = getSinglePhotoUseCase(
                    trueLocation.value.first,
                    trueLocation.value.second
                )
                if (response is Resource.Success && response.data != null) {
                    val data = response.data
                    if (data.photoUrl.isNotBlank()) {
                        reloadTime = 1
                        setImageUrl(data.photoUrl)
                        setTrueLocation(data.lat, data.lng)
                        setLoadingState(Resource.Success(Unit))
                        return@launch
                    }
                }

                reloadTime++
                if (reloadTime <= 5) {
                    val randomLoc = getRandomCityLatLngUseCase(selectedCity.value)
                    setTrueLocation(randomLoc.first, randomLoc.second)
                }
            }

            Timber.d("Max reload reached")
            reloadTime = 1
            _loadingState.emit(Resource.Error(
                context.getString(R.string.something_went_wrong),
                Exception()
            ))
        }
    }


    @SuppressLint("DefaultLocale")
    fun setGuessedLocation(lat: String, lng: String) {
        if (lat.isBlank() || lat.isBlank()) {
            _guessedLocation.value = Pair("", "")
            return
        }
        val roundedLat = String.format("%.6f", lat.toDouble())
        val roundedLng = String.format("%.6f", lng.toDouble())
        _guessedLocation.value = Pair(roundedLat, roundedLng)
    }

    fun setSelectedCity(city: City) {
        _selectedCity.value = city
        setSelectedTrueLocation(city)
    }

    fun setSelectedTrueLocation(city: City) {
        _trueLocation.value = getRandomCityLatLngUseCase(city)
        Timber.d("trueLocation ${trueLocation.value}")
    }

    fun setTrueLocation(lat: String, lng: String) {
        _trueLocation.value = Pair(lat, lng)
    }

    fun setImageUrl(url: String) {
        _imageUrl.value = url
    }

    fun setLoadingState(resource: Resource<Unit>) {
        viewModelScope.launch {
            _loadingState.emit(resource)
        }
    }

    fun setSelectedCountry(country: Country) {
        selectedCountry.value = country
    }

    fun calculateDistanceAndPoint() {
        val distance = countDistanceUseCase(
            trueLocation.value, guessedLocation.value
        )
        val point = calculatePointUseCase(distance)
        setGameHistory(
            GameHistorySinglePlayer(
                selectedCountry.value,
                selectedCity.value,
                trueLocation.value,
                guessedLocation.value,
                point,
                distance,
                gameHistory.value.size + 1
            )
        )
    }

    fun setGameHistory(gameHistorySinglePlayer: GameHistorySinglePlayer) {
        gameHistory.value = gameHistory.value.apply {
            add(gameHistorySinglePlayer)
        }
    }

    fun clearGameHistory() {
        gameHistory.value = gameHistory.value.apply {
            clear()
        }
    }

    fun setDataCityList(dataCity: List<City>) {
        cityList = dataCity.toMutableList()
    }

    fun getNewLatLng() {
        setTrueLocation("", "")
        setGuessedLocation("", "")
        setSelectedCity(getRandomCityUseCase(selectedCountry.value.id, cityList))
        val latLngCity = getRandomCityLatLngUseCase(selectedCity.value)
        setTrueLocation(latLngCity.first, latLngCity.second)
    }

}