package io.github.winfeo.superpositiongame.game.effect.effect

import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.game.effect.CardEffect
import io.github.winfeo.superpositiongame.model.card.Card

class MeasurementEffect: CardEffect {
    override fun apply(
        state: GameState,
        card: Card,
        targetSlotIndex: Int,
        playerId: String
    ): GameState {
        val player = state.players[playerId]?: return state
        val slots = player.slots.toMutableList()
        val slot = slots[targetSlotIndex]

        val updatedSlot = slot.copy(
            isFrozen = true
        )
        slots[targetSlotIndex] = updatedSlot
        val updatedPlayer = player.copy(slots = slots)

        return state.copy(
            players = state.players + (playerId to updatedPlayer)
        )
    }
}
