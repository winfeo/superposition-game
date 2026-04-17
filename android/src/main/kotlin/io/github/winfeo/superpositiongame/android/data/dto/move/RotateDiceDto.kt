package io.github.winfeo.superpositiongame.android.data.dto.move

import io.github.winfeo.superpositiongame.model.game.GameMoveType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ROTATE_DICE")
data class RotateDiceDto(
    override val playerId: String,
    val cardId: String,
    val targetSlotIndex: Int,
    val newState: String,
    val targetPlayerId: String
): MoveDto()
