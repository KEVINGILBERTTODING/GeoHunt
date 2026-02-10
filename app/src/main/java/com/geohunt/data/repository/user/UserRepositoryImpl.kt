package com.geohunt.data.repository.user

import com.geohunt.core.util.PrefHelper
import com.geohunt.data.dto.user.User
import com.geohunt.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val prefHelper: PrefHelper
) : UserRepository {
    override fun saveUserData(user: User) {
        prefHelper.saveUserData(user)
    }

    override fun getUserData(): User {
        return prefHelper.getUserData()
    }

    override fun clearUserData() {
        TODO("Not yet implemented")
    }
}