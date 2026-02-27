package com.geohunt.core.vm.multiPlayer

import androidx.lifecycle.viewModelScope
import com.geohunt.core.base.BaseViewModel
import com.geohunt.core.contract.MultiPlayerEffect
import com.geohunt.core.contract.MultiPlayerIntent
import com.geohunt.core.contract.MultiPlayerUiState
import com.geohunt.data.dto.room.RoomRoundDto
import com.geohunt.domain.usecase.GetRandomCityLatLngUseCase
import com.geohunt.domain.usecase.GetRandomCityUseCase
import com.geohunt.domain.usecase.GetSinglePhotoUseCase
import com.geohunt.domain.usecase.GetUserDataUseCase
import com.geohunt.domain.usecase.StoreRoundUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MultiPlayerVm @Inject constructor(
    private val getSinglePhotoUseCase: GetSinglePhotoUseCase,
    private val getRandomCityUseCase: GetRandomCityUseCase,
    private val getRandomCityLatLngUseCase: GetRandomCityLatLngUseCase,
    private val storeRoundUseCase: StoreRoundUseCase
): BaseViewModel<MultiPlayerIntent, MultiPlayerUiState, MultiPlayerEffect>(
    initialState = MultiPlayerUiState()
) {
    var reloadTime = 1

    fun hitKartaview() {
        viewModelScope.launch {
            storeRound(state.value.round.copy(status = "loading"))
            while (reloadTime <= 5) {
                if (state.value.trueLocPair.first.isBlank() || state.value.trueLocPair.second.isBlank()) {
                    getNewLatLng()
                }
                val response = getSinglePhotoUseCase(
                    state.value.trueLocPair.first,
                    state.value.trueLocPair.second
                )
                if (response.isSuccess) {
                    val data = response.getOrNull()
                    if (data != null && data.photoUrl.isNotBlank()) {
                        reloadTime = 1
                        updateState {
                            copy(round = RoomRoundDto(
                                photoUrl = data.photoUrl,
                                status = "success",
                                trueLat = state.value.trueLocPair.first,
                                trueLng = state.value.trueLocPair.second
                            ))
                        }
                        storeRound(state.value.round)
                        return@launch
                    }
                }

                reloadTime++
                if (reloadTime <= 5) {
                    val randomLoc = getRandomCityLatLngUseCase(state.value.city)
                    updateState { copy(trueLocPair =  randomLoc.first to randomLoc.second) }
                }
            }

            Timber.d("Max reload reached")
            reloadTime = 1
            storeRound(state.value.round.copy(status = "error"))
            sendEffect(MultiPlayerEffect.ShowToast("Something went wrong, please try again"))
        }
    }

    fun getNewLatLng() {
        updateState { copy(trueLocPair = "" to "") }
        updateState { copy(guessedLocPair = "" to "") }
        updateState { copy(city = getRandomCityUseCase(
            state.value.country.id,
            state.value.cityList)) }
        updateState { copy(trueLocPair = getRandomCityLatLngUseCase(state.value.city)) }
    }


    override suspend fun handleIntent(intent: MultiPlayerIntent) {
      when(intent) {
          is MultiPlayerIntent.OnSaveCity -> {
              updateState { copy(city = intent.city) }
          }
          is MultiPlayerIntent.OnSaveCountry -> {
              updateState { copy(country = intent.country) }
          }
          is MultiPlayerIntent.OnSaveCityList -> {
              updateState { copy(cityList = intent.cityList) }
          }
          is MultiPlayerIntent.OnLoadRoom -> {
              updateState { copy(roomData = intent.room) }
          }
          is MultiPlayerIntent.OnStartGame -> {
              if (!state.value.isRetry) {
                  updateState { copy(currentRound = state.value.roomData.rounds.size + 1) }
              }
              updateState { copy(isRetry = true) }
              hitKartaview()
          }

          is MultiPlayerIntent.OnSaveUserData -> {
              updateState { copy(userData = intent.user) }
          }
      }
    }


    fun storeRound(round: RoomRoundDto) {
        launchWithResult(
            request = { storeRoundUseCase(state.value.roomData.info.roomCode,
                round, state.value.currentRound) },
            onSuccess = {

            }
        )
    }

}