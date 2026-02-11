package com.geohunt.di.network

import com.geohunt.data.remote.GameService
import com.geohunt.data.remote.KartaviewService
import com.geohunt.di.qualifier.GameQualifier
import com.geohunt.di.qualifier.KartaviewQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @KartaviewQualifier
    fun providesKartaviewBaseUrl() = "https://api.openstreetcam.org/"

    @Provides @GameQualifier
    fun provideGameBaseUrl() = "https://raw.githubusercontent.com/"

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(providesLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @KartaviewQualifier
    fun providesKartaViewRetrofit(
        okHttpClient: OkHttpClient,
        @KartaviewQualifier baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @GameQualifier
    fun providesGameRetrofit(
        okHttpClient: OkHttpClient,
        @GameQualifier baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesKartaviewService(@KartaviewQualifier retrofit: Retrofit): KartaviewService {
        return retrofit.create(KartaviewService::class.java)
    }

    @Provides
    @Singleton
    fun providesGameService(@GameQualifier retrofit: Retrofit): GameService {
        return retrofit.create(GameService::class.java)
    }
}