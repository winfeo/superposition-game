package io.github.winfeo.superpositiongame.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LobbyResponse(
    val users: List<UserDto>
)
