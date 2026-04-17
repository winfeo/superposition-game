package io.github.winfeo.superpositiongame.android.data.dto.state

import io.github.winfeo.superpositiongame.model.card.CardType
import kotlinx.serialization.Serializable

@Serializable
data class CardDto(
    val id: String,
    val type: String
)
