package io.github.winfeo.superpositiongame.game

import io.github.winfeo.superpositiongame.model.dice.Dice

object VictoryValidator {
    fun isPlayerWin(): Boolean {
        val dices: List<Dice> = GameCycle.gameTable.getPlayerDices()
        return dices.all { it.isInRequiredState() }
    }
}
