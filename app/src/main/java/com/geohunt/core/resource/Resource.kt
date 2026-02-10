package com.geohunt.core.resource

sealed class Resource<out T> {
    object Loading: Resource<Nothing>()
    object Idle: Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String?, val exception: Exception): Resource<Nothing>()
}
