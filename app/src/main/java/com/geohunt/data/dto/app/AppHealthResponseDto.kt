package com.geohunt.data.dto.app

data class AppHealthResponseDto(
    val app_status: AppStatusDto,
    val game_config: GameConfigDto
)
