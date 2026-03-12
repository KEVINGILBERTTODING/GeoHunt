package com.geohunt.domain.repository

interface ColorRepository {
    fun getColor(index: Int): Int
    fun getTrueLocationColor(): Int
}
