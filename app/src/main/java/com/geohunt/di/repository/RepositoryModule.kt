package com.geohunt.di.repository

import com.geohunt.data.repository.country.CountryRepositoryImpl
import com.geohunt.data.repository.user.UserRepositoryImpl
import com.geohunt.domain.repository.CountryRepository
import com.geohunt.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCountryRepository(
        impl: CountryRepositoryImpl
    ): CountryRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}
