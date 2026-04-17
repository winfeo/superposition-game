package io.github.winfeo.superpositiongame.android.data.dto.state

import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.model.dice.Dice
import kotlinx.serialization.Serializable

@Serializable
data class SlotStateDto(
    val index: Int,
    val ownerId: String,
    val initialDice: DiceDto,
    val dice: DiceDto,
    val appliedCards: List<CardDto> = emptyList(),
    val isFrozen: Boolean = false
)

