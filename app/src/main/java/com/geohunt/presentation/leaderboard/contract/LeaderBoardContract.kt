package com.geohunt.presentation.leaderboard.contract

import com.geohunt.core.base.MviEffect
import com.geohunt.core.base.MviIntent
import com.geohunt.core.base.MviState
import com.geohunt.data.dto.room.RoomDto
import com.geohunt.data.dto.user.User
import com.geohunt.domain.model.LeaderBoard


data class LeaderBoardUiState(
    val leaderBoardPodiumList: List<LeaderBoard> = emptyList(),
    val leaderBoardNotPodiumList: List<LeaderBoard> = emptyList(),
    val leaderBoardState: LeaderBoardState = LeaderBoardState.Idle
): MviState

sealed class LeaderBoardIntent: MviIntent {
    object OnNavigateToHome : LeaderBoardIntent()
}

sealed class LeaderBoardEffect: MviEffect {
    data class ShowToast(val message: String): LeaderBoardEffect()
}

sealed class LeaderBoardState() {
    object Idle : LeaderBoardState()
    object Loading : LeaderBoardState()
    object Success : LeaderBoardState()
    data class Error(val message: String) : LeaderBoardState()
}
