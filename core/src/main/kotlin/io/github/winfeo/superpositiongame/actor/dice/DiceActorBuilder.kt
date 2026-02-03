package io.github.winfeo.superpositiongame.actor.dice

import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.DiceAtlasManager
import io.github.winfeo.superpositiongame.model.dice.Dice

object DiceActorBuilder {
    private val diceSide = GameConfig.getDiceSide()
    private var idCounter = 0

    fun createRandomDice(): DiceActor {
        val randomState = DiceAtlasManager.getRandomDiceState()

        val diceModel = Dice(
            id = "${randomState.stateName}_$idCounter",
            state = randomState,
        )

        val texture = DiceAtlasManager.getStateTexture(randomState.textureId)

        idCounter++
        return DiceActor(
            cardSide = diceSide,
            dice = diceModel,
            texture = texture,
            previousDice = diceModel
        )
    }

}
