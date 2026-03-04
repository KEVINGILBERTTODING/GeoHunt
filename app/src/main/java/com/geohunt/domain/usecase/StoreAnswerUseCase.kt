package com.geohunt.domain.usecase

import com.geohunt.data.dto.room.RoomAnswersDto
import com.geohunt.domain.repository.RoomRepository
import javax.inject.Inject

class StoreAnswerUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(answersDto: RoomAnswersDto, currentRound: Int, uid: String)
    : Result<Unit> = roomRepository.storeAnswer(answersDto, currentRound, uid)
}