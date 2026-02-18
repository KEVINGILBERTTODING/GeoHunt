package com.geohunt.data.dto.app

data class AppStatusDto(
    val is_health: Boolean,
    val is_maintenance: Boolean,
    val maintenance_message: String,
    val update_message: String,
    val min_version: Int,
    val latest_version: Int,
    val force_update: Boolean,
    val update_url: String
)
