package io.github.winfeo.superpositiongame.actors

import io.github.winfeo.superpositiongame.configs.GameConfig
import io.github.winfeo.superpositiongame.managers.DiceAtlasManager
import io.github.winfeo.superpositiongame.models.Dice

object DiceActorBuilder {
    private val diceSide = GameConfig.getDiceSide()

    fun createRandomDice(): DiceActor {
        val randomType = DiceAtlasManager.getRandomDiceSide()

        val diceModel = Dice(
            id = randomType,
            name = "$randomType dice side"
        )

        val texture = DiceAtlasManager.getRegion(randomType)?: throw (IllegalStateException("Не удалось найти текстуру кубита: $randomType"))
        return DiceActor(
            cardSide = diceSide,
            dice = diceModel,
            texture = texture
        )
    }

}
