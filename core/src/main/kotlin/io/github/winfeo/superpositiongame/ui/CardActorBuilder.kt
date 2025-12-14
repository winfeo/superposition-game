package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.winfeo.superpositiongame.managers.CardsAtlasManager
import io.github.winfeo.superpositiongame.models.Card

object CardActorBuilder {
    /// TODO поменять на рандомный выбор из всех карт доступных
    private val cardsList = listOf("bluecard1", "greencard1", "whitecard1", "redcard1", "yellowcard1")
    /// TODO создать файл с константами?
    //private const val CARD_SCALE = 0.3f

    fun createRandomCard(cardWidth: Float, cardHeight: Float): CardActor {
        val randomType = cardsList.random()

        val cardModel = Card(
            id = randomType,
            name = "Card $randomType",
            isFaceUp = true
        )

        val texture = CardsAtlasManager.getRegion(randomType)?: throw (IllegalStateException("Не удалось найти текстуру для карты: $randomType"))
        return CardActor(cardWidth, cardHeight, cardModel, texture)
    }

    fun createEmptyCard(cardWidth: Float, cardHeight: Float): CardActor {
        val cardModel = Card(
            id = "empty",
            name = "Empty Slot",
            isFaceUp = true
        )

        val pixmap = Pixmap(70, 120, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color(0.5f, 0.5f, 0.5f, 0.8f))
        pixmap.fill()
        val texture = Texture(pixmap)
        pixmap.dispose()

        return CardActor(cardWidth, cardHeight, cardModel, TextureRegion(texture))
    }

}
