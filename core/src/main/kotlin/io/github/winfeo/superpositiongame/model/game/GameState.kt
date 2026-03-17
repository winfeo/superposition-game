package io.github.winfeo.superpositiongame.model.game

import io.github.winfeo.superpositiongame.model.game.GamePhase
import io.github.winfeo.superpositiongame.model.game.SlotOwner

//Состояние игры (снимок)
data class GameState(
    val phase: GamePhase,
    val currentPlayerId: String,
    val players: Map<String, PlayerState>,
    val turnNumber: Int,
    val activeSlotsRow: SlotOwner? //переименовать энам? ///TODO sealed class с эффектами сделать?
) {
    companion object {
        fun initial(player1: PlayerState, player2: PlayerState): GameState {
            return GameState(
                phase = GamePhase.GAME_SETUP,
                currentPlayerId = player1.id,
                players = mapOf(
                    player1.id to player1,
                    player2.id to player2
                ),
                turnNumber = 0,
                activeSlotsRow = null
            )
        }
    }
}
