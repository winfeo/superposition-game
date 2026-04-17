package io.github.winfeo.superpositiongame.android.data.dto.state

import io.github.winfeo.superpositiongame.model.dice.DiceState
import kotlinx.serialization.Serializable

@Serializable
data class DiceDto(
    val id: String,
    var state: String,
    val requiredState: String? = null
)

