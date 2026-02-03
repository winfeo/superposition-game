package io.github.winfeo.superpositiongame.game

import com.badlogic.gdx.Gdx
import io.github.winfeo.superpositiongame.actor.card.CardFactory
import io.github.winfeo.superpositiongame.actor.dice.DiceFactory
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.game.controller.OpponentMoveController
import io.github.winfeo.superpositiongame.game.controller.PlayerMoveController
import io.github.winfeo.superpositiongame.ui.GameTimer
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.ui.GameTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executors

object GameCycle {
    val gameManager = GameManager()
    private val gameDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val gameScope = CoroutineScope(SupervisorJob() + gameDispatcher)
    lateinit var gameTable: GameTable
    lateinit var playerMoveController: PlayerMoveController
    lateinit var opponentMoveController: OpponentMoveController
    private val playerCardsAmount = GameConfig.getCardsInHandAmount()
    private val tableSlotsAmount = GameConfig.getSlotsOnTableAmount()
    private lateinit var gameTimer: GameTimer

    fun startGame(table: GameTable) {
        gameTable = table
        playerMoveController = PlayerMoveController()
        opponentMoveController = OpponentMoveController()

        gameScope.launch { gameCycle() }
    }

    private suspend fun gameCycle() {
        gameManager.changeGameState(GameState.GAME_SETUP)
        setUpGameTable()

        while (true) {
            gameManager.changeGameState(GameState.DEALING_CARDS)
            dealPlayerCards()
            gameTable.setUpCardUsage()

            startPlayerTurn()
            startOpponentTurn()
        }
    }

    fun setGameTimer(gameTimer: GameTimer) {
        this.gameTimer = gameTimer
    }

    fun getGameScope(): CoroutineScope {
        return gameScope
    }

    fun stopGame() {
        gameScope.cancel()
    }

    private suspend fun setUpGameTable() = onGdx {
        val emptyPlayerCards = List<Card>(tableSlotsAmount) {
            CardFactory.createEmptyCard()
        }
        val playerDices = List<Dice>(tableSlotsAmount) {
            DiceFactory.createRandomDice()
        }

        ///TODO создавать у оппонента отдельно?
        val emptyOpponentCards = List<Card>(tableSlotsAmount) {
            CardFactory.createEmptyCard()
        }
        val opponentDices = List<Dice>(tableSlotsAmount) {
            DiceFactory.createRandomDice()
        }

        gameTable.setUpTableActors(
            playerCards = emptyPlayerCards,
            playerDices = playerDices,
            opponentCards = emptyOpponentCards,
            opponentDices = opponentDices
        )

    }

    private suspend fun dealPlayerCards() = onGdx {
        val cards = List<Card>(playerCardsAmount) {
            CardFactory.createRandomCard()
        }

        gameTable.dealPlayerCards(cards)
    }

    suspend fun startPlayerTurn() {
        gameManager.changeGameState(GameState.PLAYER_MOVE)
        gameTimer.start {
            println("Отладка. Время игрока вышло")
            playerMoveController.finishMove()
        }
        playerMoveController.makeMove()
        gameTimer.finish()
    }

    suspend fun startOpponentTurn() {
        gameManager.changeGameState(GameState.OPPONENT_MOVE)
        gameTimer.start {
            println("Отладка. Время оппонента вышло")
            opponentMoveController.finishMove()
        }
        opponentMoveController.makeMove()
        gameTimer.finish()
    }

    suspend fun onGdx(block: () -> Unit) =
        suspendCancellableCoroutine { cont ->
            Gdx.app.postRunnable {
                block()
                cont.resume(Unit) {}
            }
        }
}
