package io.github.winfeo.superpositiongame.actor.dice

import io.github.winfeo.superpositiongame.manager.DiceAtlasManager
import io.github.winfeo.superpositiongame.model.dice.Dice

object DiceFactory {
    private var idCounter = 0

    fun createRandomDice(): Dice {

        val state = DiceAtlasManager.getRandomDiceState()

        return Dice(
            id = "${state.stateName}_${idCounter++}",
            state = state
        )
    }
}
