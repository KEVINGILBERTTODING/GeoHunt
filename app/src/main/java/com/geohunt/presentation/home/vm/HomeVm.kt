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
import com.geohunt.domain.usecase.SaveUserDataUseCase
import com.geohunt.domain.usecase.StartGameSinglePlayerUseCase
import com.geohunt.presentation.home.event.HomeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVm @Inject constructor(
    private val countryRepository: CountryRepository,
    private val userRepository: UserRepository,
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val startGameSinglePlayerUseCase: StartGameSinglePlayerUseCase,
    @ApplicationContext private val context: Context,
    private val cityRepository: CityRepository
): ViewModel() {

    private val _startGameState = MutableSharedFlow<Resource<Unit>>()
    val startGameState = _startGameState.asSharedFlow()

    private val _homeEvent = MutableSharedFlow<HomeEvent>()
    val homeEvent = _homeEvent.asSharedFlow()


    fun getAllCountry(): List<Country> {
        return countryRepository.getAllCountries()
    }

    fun getAllCity(): List<City> {
        return cityRepository.getAllCity()
    }

    fun getUserData(): User {
        return userRepository.getUserData()
    }

    fun startGameEvent() {
        viewModelScope.launch {
            _homeEvent.emit(HomeEvent.startGame)
        }
    }

    fun showCityBottomSheet() {
        viewModelScope.launch {
            _homeEvent.emit(HomeEvent.showCityBottomSheet)
        }
    }


    fun startGame(username: String, city: City, trueLocation: Pair<String, String>) {
        val uid = userRepository.getUserData().userId
        val saveUserDate = saveUserDataUseCase(username, uid)
        val startGame = startGameSinglePlayerUseCase(username, uid, city, trueLocation)
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
