package io.github.winfeo.superpositiongame.manager

import io.github.winfeo.superpositiongame.game.GameCycle
import io.github.winfeo.superpositiongame.game.controller.PlayerMoveController
import io.github.winfeo.superpositiongame.game.controller.TurnContext
import io.github.winfeo.superpositiongame.rules.RuleEngine

class MultiplicationEffectManager(
    private val moveController: PlayerMoveController
) {
    fun applyEffect() {
        TurnContext.remainingMoves += 3
        TurnContext.isMultiplicationActive = true
        GameCycle.playerMoveController.moveMade() //TODO колбэками сделать?
    }

}
