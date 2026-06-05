package io.github.winfeo.superpositiongame.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDTO (
    val id: String,
    val nickname: String?
)
