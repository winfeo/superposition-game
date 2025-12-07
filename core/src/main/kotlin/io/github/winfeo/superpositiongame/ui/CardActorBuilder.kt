package io.github.winfeo.superpositiongame.ui

import io.github.winfeo.superpositiongame.managers.CardsAtlasManager
import io.github.winfeo.superpositiongame.models.Card

object CardActorBuilder {
    /// TODO поменять на рандомный выбор из всех карт доступных
    private val cardsList = listOf("bluecard1", "greencard1", "whitecard1", "redcard1", "yellowcard1")
    /// TODO создать файл с константами?
    //private const val CARD_SCALE = 0.3f

    fun createRandomCard(): CardActor {
        val randomType = cardsList.random()

        val cardModel = Card(
            id = randomType,
            name = "Card $randomType",
            isFaceUp = true
        )

        val texture = CardsAtlasManager.getRegion(randomType)?: throw (IllegalStateException("Не удалось найти текстуру для карты: $randomType"))
        return CardActor(cardModel, texture)
    }

}
