package io.github.winfeo.superpositiongame.game.controller

import com.badlogic.gdx.scenes.scene2d.Touchable
import io.github.winfeo.superpositiongame.game.GameCycle
import io.github.winfeo.superpositiongame.game.VictoryValidator
import io.github.winfeo.superpositiongame.manager.PlayerHandManager
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayerMoveController(
    private val playerHand: PlayerHandManager
) {
    private var continuation: CancellableContinuation<Unit>? = null

    suspend fun makeMove(moves: Int = 1) = suspendCancellableCoroutine { cont ->
        TurnContext.remainingMoves = moves
        continuation = cont
    }
    fun moveMade() {
        TurnContext.remainingMoves--

        if (VictoryValidator.isPlayerWin()) {
            finishMove()
            GameCycle.stopGame()
            return
        }

        if (TurnContext.remainingMoves <= 0) {
            finishMove()
        }
    }

    fun finishMove() {
        playerHand.cards.forEach { it.touchable = Touchable.disabled }
        continuation?.resume(Unit)
        continuation = null
    }
}
