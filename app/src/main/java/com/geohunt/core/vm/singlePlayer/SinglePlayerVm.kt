package com.geohunt.core.vm.singlePlayer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.R
import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.city.City
import com.geohunt.domain.repository.CityRepository
import com.geohunt.domain.usecase.GetRandomCityLatLngUseCase
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
    private val cityRepository: CityRepository,
    private val getRandomCityLatLngUseCase: GetRandomCityLatLngUseCase,
    private val getSinglePhotoUseCase: GetSinglePhotoUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {
    val _trueLocation = MutableStateFlow(Pair("", ""))
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

    private var reloadTime = 1 // max 10

    fun getPhotos() {
        viewModelScope.launch {
            _loadingState.emit(Resource.Loading)
            val responseSinglePhotos = getSinglePhotoUseCase(
                trueLocation.value.first,
                trueLocation.value.second
            )
            if (responseSinglePhotos is Resource.Success) {
                val data = responseSinglePhotos.data
                if (data != null) {
                    val photoUrl = data.photoUrl
                    val latLng = Pair(data.lat, data.lng)
                    if (photoUrl.isNotBlank() && latLng.first.isNotBlank()
                        && latLng.second.isNotBlank()) {
                        reloadTime = 1
                        setImageUrl(photoUrl)
                        setTrueLocation(data.lat, data.lng)
                        _loadingState.emit(Resource.Success(Unit))
                    }else {
                        reloadPhotos()
                    }
                }else {
                    reloadPhotos()
                }
            }else {
                reloadPhotos()
            }
        }
    }

    fun reloadPhotos() {
        // MAX 10 times reload
        reloadTime++
        viewModelScope.launch {
            if (reloadTime > 10) {
                _loadingState.emit(Resource.Error(
                    context.getString(R.string.something_went_wrong),
                    Exception()))
                return@launch
            }
        }
        _trueLocation.value = getRandomCityLatLngUseCase(selectedCity.value)
        getPhotos()
    }

    fun setGuessedLocation(lat: String, lng: String) {
        _guessedLocation.value = Pair(lat, lng)
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


}