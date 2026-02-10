package com.geohunt.presentation.home.vm

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.R
import com.geohunt.core.resource.Resource
import com.geohunt.domain.model.country.Country
import com.geohunt.domain.model.user.User
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeVm @Inject constructor(
    private val countryRepository: CountryRepository,
    private val userRepository: UserRepository,
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val startGameSinglePlayerUseCase: StartGameSinglePlayerUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _startGameState = MutableSharedFlow<Resource<Unit>>()
    val startGameState = _startGameState.asSharedFlow()

    private val _homeEvent = MutableSharedFlow<HomeEvent>()
    val homeEvent = _homeEvent.asSharedFlow()


    fun getAllCountry(): List<Country> {
        return countryRepository.getAllCountries()
    }

    fun getUserData(): User {
        return userRepository.getUserData()
    }

    fun startGameEvent() {
        viewModelScope.launch {
            _homeEvent.emit(HomeEvent.startGame)
        }
    }

    fun startGame(username: String, country: Country, trueLocation: Pair<String, String>) {
        val uid = userRepository.getUserData().userId
        val saveUserDate = saveUserDataUseCase(username, uid)
        val startGame = startGameSinglePlayerUseCase(username, uid, country, trueLocation)
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
