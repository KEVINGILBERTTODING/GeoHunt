package com.geohunt.presentation.leaderboard.vm

import com.geohunt.core.base.BaseViewModel
import com.geohunt.domain.usecase.CalculateLeaderBoard
import com.geohunt.domain.usecase.GetRoomDataUseCase
import com.geohunt.presentation.leaderboard.contract.LeaderBoardEffect
import com.geohunt.presentation.leaderboard.contract.LeaderBoardIntent
import com.geohunt.presentation.leaderboard.contract.LeaderBoardState
import com.geohunt.presentation.leaderboard.contract.LeaderBoardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaderBoardVm @Inject constructor(
    private val getRoomDataUseCase: GetRoomDataUseCase,
    private val calculateLeaderBoard: CalculateLeaderBoard
): BaseViewModel<LeaderBoardIntent, LeaderBoardUiState, LeaderBoardEffect>(
    initialState = LeaderBoardUiState()
) {

    init {
        getRoomData()
    }

    override suspend fun handleIntent(intent: LeaderBoardIntent) {
        when(intent) {
            LeaderBoardIntent.OnNavigateToHome -> TODO()
        }
    }

    fun getRoomData() {
        launchWithResult(
            request = { getRoomDataUseCase() },
            onSuccess = { roomDto ->
                val leaderBoardMapList = calculateLeaderBoard(roomDto)
                updateState { copy(
                    leaderBoardState = LeaderBoardState.Success,
                    leaderBoardPodiumList = leaderBoardMapList.filter { it.rank in 1..3 },
                    leaderBoardNotPodiumList = leaderBoardMapList.filter { it.rank !in 1..3 },
                ) }
            }
        )
    }

    override fun onShowLoading() {
        super.onShowLoading()
        updateState { copy(leaderBoardState = LeaderBoardState.Loading) }
    }


    override fun onHandleErrorMessage(message: String) {
        super.onHandleErrorMessage(message)
        updateState { copy(leaderBoardState = LeaderBoardState.Error(message)) }
        sendEffect(LeaderBoardEffect.ShowToast(message))
    }
}