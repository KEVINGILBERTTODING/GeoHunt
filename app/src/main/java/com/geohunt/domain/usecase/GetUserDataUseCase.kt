package com.geohunt.domain.usecase

import com.geohunt.data.dto.user.User
import com.geohunt.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): User {
        return userRepository.getUserData()
    }
}
