package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.domain.game.PingRepository
import io.github.winfeo.superpositiongame.android.domain.game.usecase.ObserveGameStateUseCase
import io.github.winfeo.superpositiongame.android.domain.game.usecase.SendMoveUseCase
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameDialogState
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

//Хранит текущее состояние игры, принимает и отправляет ходы
class GameViewModel(
    private val playerId: String,
    private val gameId: String,
    private val gameRepository: GameRepository,
    private val pingRepository: PingRepository
): ViewModel() {
    private val observeGameStateUseCase = ObserveGameStateUseCase(gameRepository)
    private val sendMoveUseCase = SendMoveUseCase(gameRepository)

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState

    private val _dialogState = MutableStateFlow<GameDialogState?>(null)
    val dialogState: StateFlow<GameDialogState?> = _dialogState

    private val _timerSeconds = MutableStateFlow(0)
    val timerSeconds: StateFlow<Int> = _timerSeconds
    private var timerJob: Job? = null
    private var timerStarted = false
    private var isTimerFinished = false
    private var measuredOneWayDelayMs = 100L
    private var localTimerMs = 0L


    init {
        Log.d("GAME_MODEL", "Создание ViewModel")
//        observeGame()
//        startTimer()
        startGame()
    }

    private fun startGame() {
        viewModelScope.launch {
            try {
                val rtt = pingRepository.measureRTT()
                measuredOneWayDelayMs = rtt / 2
                Log.d("GAME_SYNC", "RTT: ${rtt}ms, OneWayDelay: ${measuredOneWayDelayMs}ms")
            } catch (e: Exception) {
                Log.e("GAME_SYNC", "Ошибка: ${e.message}")
            }

            observeGame()
            observeTimer()
            startTimerLoop()
        }
    }

    private fun observeGame() {
        viewModelScope.launch {
            observeGameStateUseCase(
                gameId = gameId,
                playerId = playerId
            ).collect { newState ->
                _gameState.value = newState
                isTimerFinished = false
            }
        }
    }

    private fun observeTimer() {
        viewModelScope.launch {
            gameRepository.observeTimerUpdates(gameId).collect { packet ->
                if (isTimerFinished) return@collect

                val correctedTimeMs = (packet.timeLeftMs - measuredOneWayDelayMs).coerceAtLeast(0L)

                if (!timerStarted) {
                    localTimerMs = correctedTimeMs
                    timerStarted = true
                } else {
                    localTimerMs =
                        if (abs(localTimerMs - correctedTimeMs) > 1500L) { correctedTimeMs }
                        else { (localTimerMs * 0.85f + correctedTimeMs * 0.15f).toLong() }
                }
            }
        }
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(100L)
                if (!timerStarted || isTimerFinished) continue

                val state = _gameState.value?: continue
                if (state.turnEndsAt <= 0L) {
                    _timerSeconds.value = 0
                    continue
                }

                localTimerMs = (localTimerMs - 100).coerceAtLeast(0L)
                _timerSeconds.value = (localTimerMs / 1000).toInt()

                if (localTimerMs <= 0 && !isTimerFinished) {
                    isTimerFinished = true
                    ///TODO "замораживать время?"
                }
            }
        }
    }

    fun sendMove(move: Move) {
        viewModelScope.launch {
            Log.d("GAME_SEND_MOVE", "Отправка хода из viewModel, ход: ${move.type}")
            Log.d("GAME_SEND_MOVE", "gameId = '$gameId', move = ${move.type}")
            sendMoveUseCase(
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
