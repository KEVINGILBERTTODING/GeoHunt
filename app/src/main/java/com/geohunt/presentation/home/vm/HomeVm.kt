package com.geohunt.presentation.home.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.R
import com.geohunt.core.resource.Resource
import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.country.Country
import com.geohunt.data.dto.user.User
import com.geohunt.domain.repository.CityRepository
import com.geohunt.domain.repository.CountryRepository
import com.geohunt.domain.repository.UserRepository
import com.geohunt.domain.usecase.GetCitiesUseCase
import com.geohunt.domain.usecase.GetCountriesUseCase
import com.geohunt.domain.usecase.GetRandomCityLatLngUseCase
import com.geohunt.domain.usecase.GetRandomCityUseCase
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.SaveUserDataUseCase
import com.geohunt.domain.usecase.StartGameSinglePlayerUseCase
import com.geohunt.presentation.home.event.HomeEvent
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
class HomeVm @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val userRepository: UserRepository,
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val startGameSinglePlayerUseCase: StartGameSinglePlayerUseCase,
    @ApplicationContext private val context: Context,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getRandomCityUseCase: GetRandomCityUseCase,
    private val getRandomCityLatLngUseCase: GetRandomCityLatLngUseCase
): ViewModel() {

    private val _startGameState = MutableSharedFlow<Resource<Unit>>()
    val startGameState = _startGameState.asSharedFlow()

    private val _homeEvent = MutableSharedFlow<HomeEvent>()
    val homeEvent = _homeEvent.asSharedFlow()

    private val _countries = MutableStateFlow(mutableListOf<Country>())
    val countries = _countries.asStateFlow()

    private val _cities = MutableStateFlow(mutableListOf<City>())
    val cities = _cities.asStateFlow()

    private val _homeState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val homeState = _homeState.asStateFlow()

    var selectedCity = City(0,0,"","", "")
    var trueLocation = Pair("", "")

    private val _userNameState = MutableStateFlow("")
    val userNameState = _userNameState.asStateFlow()

    private var uid = ""

    private val  _countryState = MutableStateFlow(Country(0,"Loading","",""))
    val countryState = _countryState.asStateFlow()




    init {
        loadUserData()
        loadGameData()
    }
    fun loadGameData(){
        viewModelScope.launch {
            _homeState.value = Resource.Loading
            val responseCountries = getCountriesUseCase()
            val responseCities = getCitiesUseCase()
            if (responseCountries is Resource.Success
                && responseCities is Resource.Success) {
                if (responseCountries.data.isNotEmpty()
                    && responseCities.data.isNotEmpty()){
                    _countryState.value = responseCountries.data.first()
                    _countries.value = responseCountries.data.toMutableList()
                    _cities.value = responseCities.data.toMutableList()
                    setSelectedCity(countries.value.first())
                    setSelectedCity(countryState.value)
                    _homeState.value = Resource.Success(Unit)
                }else {
                    _countryState.value = Country(0, "Error", "", "")
                    _homeState.value = Resource.Error(
                        context.getString(R.string.unable_to_load_data),
                        Exception())
                }
            }else {
                _countryState.value = Country(0, "Error", "", "")
                _homeState.value = Resource.Error(
                    context.getString(R.string.something_went_wrong),
                    Exception())
            }
        }
    }

    fun loadUserData() {
        val dataUser = getUserData()
        setUserName(dataUser.username.ifEmpty { "Guest" })
        uid = dataUser.userId
    }

    fun setSelectedCountry(country: Country) {
        _countryState.value = country
    }


    fun getUserData(): User {
        return getUserDataUseCase()
    }

    fun startGameEvent() {
        viewModelScope.launch {
            _homeEvent.emit(HomeEvent.startGame)
        }
    }

    fun showCountryBottomSheet() {
        viewModelScope.launch {
            _homeEvent.emit(HomeEvent.showCountryBottomSheet)
        }
    }

    fun setUserName(username: String) {
        _userNameState.value = username
    }

    fun setSelectedCity(country: Country) {
        selectedCity = getRandomCityUseCase(country.id, cities.value)
        trueLocation = getRandomCityLatLngUseCase(selectedCity)
    }


    fun startGame() {
        val saveUserDate = saveUserDataUseCase(userNameState.value, uid)
        val startGame = startGameSinglePlayerUseCase(userNameState.value, uid, selectedCity, trueLocation)
        viewModelScope.launch {
            if (saveUserDate is Resource.Error) {
                _startGameState.emit(Resource.Error("${saveUserDate.message}", Exception()))
                return@launch
            }
            if (!startGame) {
                _startGameState.emit(Resource.Error(context.getString(R.string.start_game_failed), Exception()))
                return@launch
            }
            _startGameState.emit(Resource.Success(Unit))
        }
    }


}
