package io.github.winfeo.superpositiongame.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameManager {
    private var _gameState = MutableStateFlow(GameState.DEALING_CARDS)
    val state = _gameState.asStateFlow()

    fun changeGameState(newState: GameState) {
        _gameState.value = newState
    }

    fun isPlayerMove(): Boolean = _gameState.value == GameState.PLAYER_MOVE
}
