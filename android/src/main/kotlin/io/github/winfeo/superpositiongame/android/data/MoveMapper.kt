package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.MoveDto
import io.github.winfeo.superpositiongame.model.game.GameMoveType
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.model.game.Move.*
import io.github.winfeo.superpositiongame.model.dice.DiceState

fun MoveDto.toDomain(): Move {
    return when (type) {
        GameMoveType.PLAY_CARD -> PlayCard(
            playerId = playerId,
            cardId = payload!!["cardId"] as String,
            targetSlotIndex = (payload["targetSlotIndex"] as Long).toInt(),
            targetPlayerId = payload["targetPlayerId"] as String
        )

        GameMoveType.ROTATE_DICE -> RotateDice(
            playerId = playerId,
            cardId = payload!!["cardId"] as String,
            targetSlotIndex = (payload["targetSlotIndex"] as Long).toInt(),
            newState = DiceState.valueOf(payload["newState"] as String),
            targetPlayerId = payload["targetPlayerId"] as String
        )

        GameMoveType.SWAP_DICES -> SwapDices(
            playerId = playerId,
            firstSlotIndex = (payload!!["firstSlotIndex"] as Long).toInt(),
            secondSlotIndex = (payload["secondSlotIndex"] as Long).toInt()
        )

        GameMoveType.START_GAME -> StartGame(
            playerId = playerId,
            playerRandomDices = convertDiceStateMap(payload!!["playerRandomDices"] as Map<String, List<String>>),
            playerRequiredDices = convertDiceStateMap(payload["playerRequiredDices"] as Map<String, List<String>>)
        )

        GameMoveType.DEAL_CARDS -> DealCards(
            playerId = playerId,
            playersNewCards = payload!!["playersNewCards"] as Map<String, List<String>>
        )

        GameMoveType.BEGIN_TURN -> BeginTurn(playerId = playerId)

        GameMoveType.END_TURN -> EndTurn(playerId = playerId)
    }
}


fun Move.toDto(): MoveDto {
    val payload = when (this) {
        is StartGame -> mapOf(
            "playerRandomDices" to playerRandomDices.mapValues { it.value.map { diceState -> diceState.name } },
            "playerRequiredDices" to playerRequiredDices.mapValues { it.value.map { diceState -> diceState.name } }
        )

        is PlayCard -> mapOf(
            "cardId" to cardId,
            "targetSlotIndex" to targetSlotIndex,
            "targetPlayerId" to targetPlayerId
        )

        is RotateDice -> mapOf(
            "cardId" to cardId,
            "targetSlotIndex" to targetSlotIndex,
            "newState" to newState.name,
            "targetPlayerId" to targetPlayerId
        )

        is SwapDices -> mapOf(
            "firstSlotIndex" to firstSlotIndex,
            "secondSlotIndex" to secondSlotIndex
        )

        is DealCards -> mapOf(
            "playersNewCards" to playersNewCards
        )

        is BeginTurn -> null
        is EndTurn -> null
    }

    return MoveDto(
        type = type,
        playerId = playerId,
        payload = payload
    )
}

private fun convertDiceStateMap(input: Map<String, List<String>>): Map<String, List<DiceState>> {
    return input.mapValues { (_, stringList) ->
        stringList.map { stringValue ->
            DiceState.valueOf(stringValue)
        }
    }
}
