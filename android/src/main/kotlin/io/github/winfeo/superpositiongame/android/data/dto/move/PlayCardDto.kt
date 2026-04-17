package io.github.winfeo.superpositiongame.android.data.dto.move

import io.github.winfeo.superpositiongame.model.game.GameMoveType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PLAY_CARD")
data class PlayCardDto(
    override val playerId: String,
    val cardId: String,
    val targetSlotIndex: Int,
    val targetPlayerId: String,
): MoveDto()
