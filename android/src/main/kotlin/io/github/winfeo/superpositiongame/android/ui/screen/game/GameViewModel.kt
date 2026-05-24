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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val _timerSeconds = MutableStateFlow(0)
    val timerSeconds: StateFlow<Int> = _timerSeconds
    private var timerJob: Job? = null
    private var isTimerFinished = false
    private var lastServerTime: Long = 0L
    private var lastClientTime: Long = 0L


    init {
        Log.d("GAME_MODEL", "Создание ViewModel")
        observeGame()
        startTimer()
    }

    private fun observeGame() {
        viewModelScope.launch { //TODO переделать на use case
            repository.observeGameState(gameId, playerId).collect { newState ->
                _gameState.value = newState

                lastServerTime = newState.serverTime
                lastClientTime = System.currentTimeMillis()
                isTimerFinished = false
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

    fun startTimer() {
        timerJob?.cancel()
//        isTimerFrozen = false //TODO убать LaunchedEffect таймера из GameActivity? Всё-равно сбрасывается при окончании игры

//        timerJob = viewModelScope.launch {
//            while (_timerSeconds.value > 0) {
//                delay(1000L)
//                if (!isTimerFrozen) {
//                    _timerSeconds.value -= 1
//                }
//            }
//            if (!isTimerFrozen) {
//                onTimerFinished()
//            }
//        }
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)

                if (isTimerFinished) continue

                val state = _gameState.value?: continue
                if (state.turnEndsAt <= 0L) {
                    _timerSeconds.value = 0
                    continue
                }

                val localTimeNow = System.currentTimeMillis()
                val timePassedClient = localTimeNow - lastClientTime //локально времени прошло
                val timePassedServer = lastServerTime + timePassedClient //время сервера (предполож)
                val remainingTime = state.turnEndsAt - timePassedServer //остаток времени

                val seconds = (remainingTime / 1000L).coerceAtLeast(0L).toInt()
                _timerSeconds.value = seconds

                if (seconds <= 0 && !isTimerFinished) {
                    isTimerFinished = true
                    ///TODO "замораживать время?"
                }

            }

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

    fun showCardPreview(
        card: Card
    ) {
        _dialogState.value = GameDialogState.CardPreviewDialog(
            card = card
        )
    }

    fun showGameFinishedDialog(
        isWinner: Boolean,
        onReturnToLobby: () -> Unit
    ) {
        isTimerFinished = true

        _dialogState.value = GameDialogState.GameFinishedDialog(
            isWinner = isWinner,
            onReturnToLobby = onReturnToLobby
        )
    }

    fun showGameMenuDialog(
        onResume: () -> Unit,
        onRules: () -> Unit,
        onSettings: () -> Unit,
        onSurrender: () -> Unit,
        onDismiss: () -> Unit
    ) {
        _dialogState.value = GameDialogState.GameMenuDialog(
            onResume = onResume,
            onRules = onRules,
            onSettings = onSettings,
            onSurrender = onSurrender,
            onDismiss = onDismiss
        )
    }

    fun dismissDialog() {
        _dialogState.value = null
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        timerJob = null
    }
}
