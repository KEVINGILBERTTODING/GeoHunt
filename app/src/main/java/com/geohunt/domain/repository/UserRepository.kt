package com.geohunt.domain.repository

import com.geohunt.data.dto.user.User

interface UserRepository {
    fun saveUserData(user: User)
    fun getUserData(): User
    fun clearUserData()
}