package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.winfeo.superpositiongame.model.card.CardFactory
import io.github.winfeo.superpositiongame.model.dice.DiceFactory
import io.github.winfeo.superpositiongame.android.data.repository.GameRepositoryImpl
import io.github.winfeo.superpositiongame.game.GameEngine
import io.github.winfeo.superpositiongame.model.game.GameMoveType
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.model.game.PlayerState
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.model.dice.DiceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Хранит текущее состояние игры, принимает и отправляет ходы
class GameViewModel(
    private val playerId: String,
    private val gameId: String
): ViewModel() {
    private val database = Firebase.database ///TODO переделать
    private val repository = GameRepositoryImpl(database)
    private val engine = GameEngine()

    private val _ids = MutableStateFlow<List<String>?>(null) //TODO переделать
    val ids: StateFlow<List<String>?> = _ids
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState
    private var dealCardsFlag: Boolean = false ///TODO переделать

    init {
        viewModelScope.launch {
            repository.getPlayerIds(gameId).collect { ids ->
                Log.d("Debugg", "ViewModel. $ids")
                _ids.value = ids

                if (ids.size < 2) return@collect
                startGame(ids)
            }

        }
    }

    //TODO переделать, вынести на сервер
    private suspend fun startGame(ids: List<String>) {
        Log.d("Debugg", "ViewModel. GameStarted")
        if (_gameState.value != null) return
        Log.d("Debugg", "ViewModel. GameState: ${_gameState.value}")


        val initialState = GameState.initial(
            player1 = PlayerState(id = ids[0]),
            player2 = PlayerState(id = ids[1])
        )

        _gameState.value = initialState

        Log.d("Debugg", "ViewModel. GameState: ${_gameState.value}")

        observeMoves()
        tryStartGame(ids)
    }

    private suspend fun tryStartGame(ids: List<String>) {
        val hostId = ids.first()
        Log.d("Debugg", "ViewModel. Host: $hostId")

        if (playerId != hostId) return

        val move = Move.StartGame(
            playerId = playerId,
            type = GameMoveType.START_GAME,
            playerRandomDices = generateRandomDices(ids),
            playerRequiredDices = generateRequiredDices(ids)
        )

        repository.sendMove(gameId, move)
    }

    private fun generateRandomDices(ids: List<String>): Map<String, List<DiceState>> {
        return ids.associateWith {
            List(GameConfig.getSlotsOnTableAmount()) {
                DiceFactory.getRandomDiceState()
            }
        }
    }

    private fun generateRequiredDices(ids: List<String>): Map<String, List<DiceState>> {
        return ids.associateWith {
            List(GameConfig.getSlotsOnTableAmount()) {
                DiceFactory.getRequiredDiceState()
            }
        }
    }

    private fun observeMoves() {
        viewModelScope.launch {
            repository.receiveMove(gameId).collect { move ->
                val currentState = _gameState.value?: return@collect

                val newGameState = engine.applyMove(
                    currentState = currentState,
                    move = move
                )

                _gameState.value = newGameState

                handleAutoMoves(newGameState, move) ///TODO убрать потом
            }
        }
    }

    private suspend fun handleAutoMoves( ///TODO перенести на сервер
        state: GameState,
        move: Move
    ) {
        val hostId = ids.value!!.first()
        if (playerId != hostId) return

        when(move) {
            is Move.StartGame -> {
                val dealMove = Move.DealCards(
                    playerId = playerId,
                    type = GameMoveType.DEAL_CARDS,
                    playersNewCards = generateCards(ids.value!!)
                )
                repository.sendMove(gameId, dealMove)
            }

            is Move.DealCards -> {
                val beginTurn = Move.BeginTurn(
                    playerId = state.currentPlayerId,
                    type = GameMoveType.BEGIN_TURN
                )
                repository.sendMove(gameId, beginTurn)
            }

            is Move.PlayCard, is Move.RotateDice, is Move.SwapDices -> {
                val endTurn = Move.EndTurn(
                    playerId = state.currentPlayerId,
                    type = GameMoveType.END_TURN
                )

                repository.sendMove(gameId, endTurn)
            }

            is Move.BeginTurn -> {
//                val endTurn = Move.EndTurn(
//                    playerId = gameState.value!!.currentPlayerId,
//                    type = GameMoveType.END_TURN
//                )
//
//                repository.sendMove(gameId, endTurn)
            }

            is Move.EndTurn -> {
                val nextPlayer = state.players.keys.first { it != move.playerId}
                if (dealCardsFlag) {
                    dealCardsFlag = false
                    val dealMove = Move.DealCards(
                        playerId = nextPlayer,
                        type = GameMoveType.DEAL_CARDS,
                        playersNewCards = generateCards(ids.value!!)
                    )
                    repository.sendMove(gameId, dealMove)
                }
                else {
                    dealCardsFlag = true
                    val beginTurn = Move.BeginTurn(
                        playerId = nextPlayer,
                        type = GameMoveType.BEGIN_TURN
                    )
                    repository.sendMove(gameId, beginTurn)
                }
            }
        }
    }

    private fun generateCards(ids: List<String>): Map<String, List<String>> {
        return ids.associateWith {
            List(GameConfig.getCardsInHandAmount()) {
                CardFactory.getRandomCardName()
            }
        }
    }

    fun sendMove(move: Move) {
        viewModelScope.launch {
            repository.sendMove(
                gameId = gameId,
                move = move
            )
        }
    }
}
