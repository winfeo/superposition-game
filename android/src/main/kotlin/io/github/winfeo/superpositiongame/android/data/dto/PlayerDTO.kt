package io.github.winfeo.superpositiongame.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameUserDTO (
    val id: String,
    val nickname: String?
)
