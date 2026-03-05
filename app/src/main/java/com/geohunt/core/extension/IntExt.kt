package com.geohunt.core.extension

fun Int.formatTime(): String {
    val minutes = this / 60
    val secs = this % 60
    return "%02d:%02d".format(minutes, secs)
}