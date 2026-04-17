package io.github.winfeo.superpositiongame.android.data.dto.move

import io.github.winfeo.superpositiongame.model.game.GameMoveType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SWAP_DICES")
data class SwapDicesDto(
    override val playerId: String,
    val firstSlotIndex: Int,
    val secondSlotIndex: Int,
): MoveDto()
