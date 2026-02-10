package com.geohunt.core.util

import android.content.Context
import javax.inject.Inject
import androidx.core.content.edit
import com.geohunt.data.dto.user.User

class PrefHelper @Inject constructor(
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun saveUserData(user: User) {
        saveString("username", user.username)
        saveString("userId", user.userId)
    }

    fun getUserData() : User {
        return User(
            getString("username", ""),
            getString("userId", "")
        )
    }

    fun saveString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

}