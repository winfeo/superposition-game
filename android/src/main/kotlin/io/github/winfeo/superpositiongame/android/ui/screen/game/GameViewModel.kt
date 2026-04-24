package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.repository.GameRepositoryImpl
import io.github.winfeo.superpositiongame.android.ui.dialog.GameDialogState
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState
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

    private val _dialogState = MutableStateFlow<GameDialogState?>(null)
    val dialogState: StateFlow<GameDialogState?> = _dialogState

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
            Log.d("GAME_SEND_MOVE", "Отправка хода из viewModel, ход: ${move.type}")
            Log.d("GAME_SEND_MOVE", "gameId = '$gameId', move = ${move.type}")
            repository.sendMove(
                gameId = gameId,
                move = move
            )
        }
    }

    fun showRotateCardDialog(
        availableStates: List<DiceState>,
        onStateSelected: (DiceState) -> Unit
    ) {
        _dialogState.value = GameDialogState.RotateDialog(
            availableStates = availableStates,
            onStateSelected = onStateSelected
        )
    }

    fun showReshuffleDialog(
        cards: List<Card>,
        maxSelectable: Int = 4,
        minSelectable: Int = 1,
        onCardsSelected: (List<Card>) -> Unit
    ) {
        _dialogState.value = GameDialogState.ReshuffleDialog(
            cards = cards,
            maxSelectable = maxSelectable,
            minSelectable = minSelectable,
            onCardsSelected = onCardsSelected
        )
    }

    fun dismissDialog() {
        _dialogState.value = null
    }
}
