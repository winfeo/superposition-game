package io.github.winfeo.superpositiongame.ui.actor.dice

import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.model.dice.Dice

object DiceActorBuilder {
    private val diceSide = GameConfig.getDiceSide()

//    fun createRandomDice(dice: Dice): DiceActor {
//        val texture = DiceAtlasManager.getStateTexture(dice.state.textureId)
//
//        return DiceActor(
//            cardSide = diceSide,
//            dice = dice,
//            texture = texture,
//            previousDice = dice ///TODO вынести отсюда в слот (контейнер)?
//        )
//    }

    fun buildDiceActor(dice: Dice): DiceActor {
        return DiceActor(
            sideSize = diceSide,
            dice = dice
        )
    }

}
