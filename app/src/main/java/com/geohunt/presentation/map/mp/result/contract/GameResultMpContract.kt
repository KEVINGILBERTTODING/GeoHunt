package com.geohunt.presentation.map.mp.result.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.data.dto.user.User
import com.geohunt.domain.model.Answer
import com.geohunt.domain.model.RoundResult
import com.geohunt.domain.model.LeaderBoard
import com.geohunt.domain.model.Room
import com.geohunt.domain.model.RoomInfo
import com.geohunt.domain.model.Round
import com.google.maps.android.compose.CameraPositionState

data class GameResultMpUiState(
    val isLoading: Boolean = false,
    val error: String? = "",
    val isLoadingStartGame: Boolean = false,
    val isLoadingBack : Boolean = false,
    val isHost: Boolean = false,
    val userData: User = User("", ""),
    val cameraPositionState: CameraPositionState = CameraPositionState(),
    val leaderBoardList: List<LeaderBoard> = emptyList(),
    val point: Int = 0,
    val answerList: List<Answer> = emptyList(),
    val roundResultList: List<RoundResult> = emptyList(),
    val round: Round = Round(),
    val trueLocColor: Int = 0,
    val isFinished: Boolean = false,
    val currentRoundIndex: Int = -1,
    val endTime: Long = 0,
    val timeLeft: Int = 0,
    val gameResultState: GameResultState = GameResultState.Idle
): MviState

sealed class GameResultMpIntent: MviIntent {
    object OnBackPressed : GameResultMpIntent()
    data class OnSetCameraState(val lat: String, val lng: String) : GameResultMpIntent()
}

sealed class GameResultMpEffect: MviEffect {
    data class ShowToast(val message: String): GameResultMpEffect()
    object OnNavigateToMap : GameResultMpEffect()
    object OnBack : GameResultMpEffect()
    object OnStartGame : GameResultMpEffect()
}

sealed class GameResultState() {
    object Idle : GameResultState()
    object Loading : GameResultState()
    object Success : GameResultState()
    object Error : GameResultState()
}
