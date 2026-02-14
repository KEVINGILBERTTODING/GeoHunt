package com.geohunt.core.extension

fun Float.toPrettierDistanceString(): String {
    return if (this < 1000) {
        "${this.toInt()} m"
    } else {
        "%.2f km".format(this / 1000)
    }
}