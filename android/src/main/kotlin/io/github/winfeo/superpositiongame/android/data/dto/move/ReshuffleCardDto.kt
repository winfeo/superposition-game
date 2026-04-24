package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RESHUFFLE_CARD")
data class ReshuffleCardDto(
    override val playerId: String,
    val cardId: String,
    val cardsToChange: List<String>
): MoveDto()
