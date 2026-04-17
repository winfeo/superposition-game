package io.github.winfeo.superpositiongame.android.data.dto.state

import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.game.SlotState
import kotlinx.serialization.Serializable

@Serializable
data class PlayerStateDto(
    val id: String,
    val hand: List<CardDto> = emptyList(),
    val slots: List<SlotStateDto> = emptyList(),
    val skipNextTurn: Boolean = false,
    val remainingMoves: Int = 1
)
