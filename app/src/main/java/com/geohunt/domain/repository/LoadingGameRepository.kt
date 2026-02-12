package com.geohunt.domain.repository

interface LoadingGameRepository {
    fun getLoadingMessage(): List<String>
    fun getTipsMessage(): List<String>
}