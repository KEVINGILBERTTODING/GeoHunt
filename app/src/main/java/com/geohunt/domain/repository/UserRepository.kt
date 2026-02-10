package com.geohunt.domain.repository

import com.geohunt.domain.model.user.User

interface UserRepository {
    fun saveUserData(user: User)
    fun getUserData(): User
    fun clearUserData()
}