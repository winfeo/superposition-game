package io.github.winfeo.superpositiongame.game.effect.effect

import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.game.effect.CardEffect
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.rule.rule.ArrowCompatibilityRule

class PauliEffect: CardEffect {
    ///TODO ещё зависит от оси же
    override fun apply(
        state: GameState,
        card: Card,
        targetSlotIndex: Int,
        playerId: String
    ): GameState {
        val player = state.players[playerId]?: return state
        val slots = player.slots.toMutableList()

        val arrowRule = ArrowCompatibilityRule()

        val indexes =
            if (card.actionRadius == 3) listOf(targetSlotIndex - 1, targetSlotIndex, targetSlotIndex + 1)
            else listOf(targetSlotIndex)

        indexes.forEach { index ->
            val slot = slots[index]

            if (index != targetSlotIndex) {
                if (!arrowRule.isCardCompatibleWithArrow(card, slot.dice)) {
                    return@forEach
                }
            }

            val newDice = calculateNewDiceState(
                currentState = slot.dice.state,
                cardAxis = card.axis?: return state
            )

            slots[index] = slot.copy(
                dice = slot.dice.copy(state = newDice),
                appliedCards =
                    if (index == targetSlotIndex) slot.appliedCards + card
                    else slot.appliedCards
            )
        }

        val updatedPlayer = player.copy(slots = slots)
        return state.copy(
            players = state.players + (playerId to updatedPlayer)
        )
    }

    private fun calculateNewDiceState(currentState: DiceState, cardAxis: AxisRotation): DiceState {
        return when (cardAxis) {
            AxisRotation.X -> when (currentState) {
                DiceState.ZERO -> DiceState.ONE
                DiceState.ONE -> DiceState.ZERO
                DiceState.I -> DiceState.I_MINUS
                DiceState.I_MINUS -> DiceState.I
                else -> currentState
            }
            AxisRotation.Y -> when (currentState) {
                DiceState.PLUS -> DiceState.MINUS
                DiceState.MINUS -> DiceState.PLUS
                DiceState.ZERO -> DiceState.ONE
                DiceState.ONE -> DiceState.ZERO
                else -> currentState
            }
            AxisRotation.Z -> when (currentState) {
                DiceState.PLUS -> DiceState.MINUS
                DiceState.MINUS -> DiceState.PLUS
                DiceState.I -> DiceState.I_MINUS
                DiceState.I_MINUS -> DiceState.I
                else -> currentState
            }
        }
    }
}
