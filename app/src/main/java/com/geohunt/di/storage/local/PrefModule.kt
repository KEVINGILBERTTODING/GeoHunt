package com.geohunt.di.storage.local

import android.content.Context
import com.geohunt.core.util.PrefHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefModule {
    @Provides
    @Singleton
    fun providePrefHelper(@ApplicationContext context: Context): PrefHelper {
        return PrefHelper(context)
    }
}