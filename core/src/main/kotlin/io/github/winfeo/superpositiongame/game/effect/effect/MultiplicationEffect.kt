package io.github.winfeo.superpositiongame.game.effect.effect

import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.game.effect.CardEffect
import io.github.winfeo.superpositiongame.model.card.Card

class MultiplicationEffect: CardEffect {
    override fun apply(
        state: GameState,
        card: Card,
        targetSlotIndex: Int,
        playerId: String
    ): GameState {
        val player = state.players[playerId]?: return state

        val updatedPlayer = player.copy(remainingMoves = player.remainingMoves + 3)
        return state.copy(
            players = state.players + (playerId to updatedPlayer)
        )
    }
}
