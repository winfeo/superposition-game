package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.repository.GameRepositoryImpl
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Хранит текущее состояние игры, принимает и отправляет ходы
class GameViewModel(
    private val playerId: String,
    private val gameId: String
): ViewModel() {
    private val repository = GameRepositoryImpl()
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState

    init {
        Log.d("GAME_MODEL", "Создание ViewModel")
        observeGame()
    }

    private fun observeGame() {
        viewModelScope.launch { //TODO переделать на use case
            repository.observeGameState(gameId, playerId).collect { newState ->
                _gameState.value = newState
            }
        }
    }

    fun sendMove(move: Move) {
        viewModelScope.launch { //TODO переделать на use case
            repository.sendMove(
                gameId = gameId,
                move = move
            )
        }
    }
}
