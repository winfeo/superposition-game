package io.github.winfeo.superpositiongame.android.data

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.dto.state.CardDto
import io.github.winfeo.superpositiongame.android.data.dto.state.DiceDto
import io.github.winfeo.superpositiongame.android.data.dto.state.GameStateDto
import io.github.winfeo.superpositiongame.android.data.dto.state.PlayerStateDto
import io.github.winfeo.superpositiongame.android.data.dto.state.SlotStateDto
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardFactory
import io.github.winfeo.superpositiongame.model.card.CardTypeNew
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GamePhase
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.PlayerState
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.model.game.SlotState

fun GameStateDto.toDomain(playerId: String): GameState {
    return GameState(
        phase = GamePhase.valueOf(this.phase),
        currentPlayerId = this.currentPlayerId,
        players = this.players.mapValues { (_, playerDto) ->
            playerDto.toDomain(playerId)
        },
        turnNumber = this.turnNumber,
        activeSlotsRow = slotOwnerOrNull(
            playerId = playerId,
            slotsId = this.activeSlotsRow
        )
    )
}

fun PlayerStateDto.toDomain(playerId: String): PlayerState {
    return PlayerState(
        id = this.id,
        hand = this.hand.map { it.toDomain() },
        slots = this.slots.map { it.toDomain(id) },
        skipNextTurn = this.skipNextTurn,
        remainingMoves = this.remainingMoves
    )
}

fun SlotStateDto.toDomain(playerId: String): SlotState {
    return SlotState(
        index = this.index,
        slotOwner = if (this.ownerId == playerId) SlotOwner.PLAYER else SlotOwner.OPPONENT,
        initialDice = this.initialDice.toDomain(),
        dice = this.dice.toDomain(),
        appliedCards = this.appliedCards.map { it.toDomain() },
        isFrozen = this.isFrozen
    )
}

fun DiceDto.toDomain(): Dice {
    return Dice(
        id = this.id,
        state = DiceState.valueOf(this.state),
        requiredState = DiceState.valueOf(this.state)
    )
}

fun CardDto.toDomain(): Card {
    val type = CardTypeNew.valueOf(this.type)
    val card = CardFactory.buildCardFromType(type, this.id)
    return card;
}

private fun slotOwnerOrNull(playerId: String, slotsId: String?): SlotOwner? {
    Log.d("GAME_SLOT", "Маппинг. PlayerId: $playerId" +
        "SlotId: $slotsId")
//    val slotOwner = try {
//        if (playerId == slotsId) SlotOwner.PLAYER else SlotOwner.OPPONENT
//    } catch (e: Exception) {
//        null
//    }

    if (slotsId.isNullOrEmpty()) return null
    val slotOwner = if (playerId == slotsId) SlotOwner.PLAYER else SlotOwner.OPPONENT

    Log.d("GAME_SLOT", "Слот овнер: $slotOwner")
    return slotOwner
}
