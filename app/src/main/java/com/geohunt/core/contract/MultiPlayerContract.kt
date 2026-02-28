package com.geohunt.core.contract

import android.service.autofill.UserData
import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.data.dto.city.City
import com.geohunt.data.dto.country.Country
import com.geohunt.data.dto.room.RoomRoundDto
import com.geohunt.data.dto.user.User
import com.geohunt.domain.model.Room
import com.geohunt.domain.model.Round

sealed class MultiPlayerIntent: MviIntent {
    data class OnSaveCountry(val country: Country): MultiPlayerIntent()
    data class OnSaveCity(val city: City): MultiPlayerIntent()
    data class OnSaveCityList(val cityList: List<City>): MultiPlayerIntent()
    data class OnStartGame(val currentRound: Int) : MultiPlayerIntent()
    data class OnSaveUserData(val user: User): MultiPlayerIntent()
}

sealed class MultiPlayerEffect: MviEffect {
    data class ShowToast(val message: String): MultiPlayerEffect()
    object OnSuccess : MultiPlayerEffect()
    object OnBack : MultiPlayerEffect()
}

data class MultiPlayerUiState(
    val country: Country = Country(0, "Random", "", ""),
    val city: City = City(0,0, "","", ""),
    val trueLocPair: Pair<String, String> = Pair("", ""),
    val guessedLocPair: Pair<String, String> = Pair("", ""),
    val cityList: List<City> = emptyList(),
    val round: RoomRoundDto = RoomRoundDto(),
    val currentRound: Int = 0,
    val isRetry: Boolean = false,
    val userData: User = User("", "")
): MviState