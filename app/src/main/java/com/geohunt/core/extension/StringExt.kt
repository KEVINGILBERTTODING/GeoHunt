package com.geohunt.core.extension

fun String.roundCoord(decimals: Int = 6): String {
    return this.toDoubleOrNull()?.let { "%.${decimals}f".format(it) } ?: this
}