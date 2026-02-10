package com.geohunt.domain.usecase

import android.content.Context
import com.geohunt.R
import com.geohunt.core.resource.Resource
import com.geohunt.domain.model.user.User
import com.geohunt.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SaveUserDataUseCase @Inject constructor(
    private val userDataRepository: UserRepository,
    private val generateUidUseCase: GenerateUidUseCase,
    @ApplicationContext private val context: Context
) {
    operator fun invoke(username: String, uid: String): Resource<Unit> {
        if (username.isBlank()) return Resource.Error(context.getString(R.string.username_cannot_be_empty), Exception())
        if (username.length < 3) return Resource.Error(context.getString(R.string.username_must_be_at_least_3_character), Exception())
        if (username.length > 30) return Resource.Error(context.getString(R.string.username_must_be_less_than_30_character), Exception())
        val userId = uid.takeIf { it.isNotEmpty() } ?: generateUidUseCase()
        userDataRepository.saveUserData(User(username, userId))
        return Resource.Success(Unit)
    }
}