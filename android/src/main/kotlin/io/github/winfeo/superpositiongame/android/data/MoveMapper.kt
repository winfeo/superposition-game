package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.move.DoubleTapEffectDto
import io.github.winfeo.superpositiongame.android.data.dto.move.MoveDto
import io.github.winfeo.superpositiongame.android.data.dto.move.PlayCardDto
import io.github.winfeo.superpositiongame.android.data.dto.move.RotateDiceDto
import io.github.winfeo.superpositiongame.android.data.dto.move.SwapDicesDto
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.model.game.Move.*

fun Move.toDto(): MoveDto {
    return when (this) {
        is PlayCard -> PlayCardDto(
            playerId = playerId,
            cardId = cardId,
            targetSlotIndex = targetSlotIndex,
            targetPlayerId = targetPlayerId
        )
        is RotateDice -> RotateDiceDto(
            playerId = playerId,
            cardId = cardId,
            targetSlotIndex = targetSlotIndex,
            newState = newState.name,
            targetPlayerId = targetPlayerId
        )
        is SwapDices -> SwapDicesDto(
            playerId = playerId,
            cardId = cardId,
            firstSlotIndex = firstSlotIndex,
            secondSlotIndex = secondSlotIndex,
            firstSlotOwner = firstSlotOwner,
            secondSlotOwner = secondSlotOwner
        )
        is DoubleTapEffect -> DoubleTapEffectDto(
            playerId = playerId,
            cardId = cardId
        )
    }
}

