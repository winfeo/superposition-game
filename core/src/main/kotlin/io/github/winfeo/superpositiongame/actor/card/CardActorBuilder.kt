package io.github.winfeo.superpositiongame.actor.card

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.CardsAtlasManager
import io.github.winfeo.superpositiongame.model.Card
import io.github.winfeo.superpositiongame.model.CardInstruction
import io.github.winfeo.superpositiongame.util.CardNameParser

object CardActorBuilder {
    private val cardWidth = GameConfig.cardWidth
    private val cardHeight = GameConfig.cardHeight

    fun createRandomCard(): CardActor {
        val randomName = CardsAtlasManager.getRandomCardId()

        val cardModel = Card(
            id = randomName,
            name = "Card $randomName",
            instruction = CardNameParser.createInstructionFromCardName(randomName)
        )

        val texture = CardsAtlasManager.getRegion(randomName)?: throw (IllegalStateException("Не удалось найти текстуру для карты: $randomName"))
        return CardActor(
            cardWidth = cardWidth,
            cardHeight = cardHeight,
            card = cardModel,
            canDrag = true,
            texture = texture
        )
    }

    fun createEmptyCard(): CardActor {
        val cardModel = Card(
            id = "empty",
            name = "Empty Slot",
            instruction = CardInstruction()
        )

        val pixmap = Pixmap(70, 120, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.CLEAR)
        pixmap.fill()
        val texture = Texture(pixmap)
        pixmap.dispose()

        return CardActor(
            cardWidth = cardWidth,
            cardHeight = cardHeight,
            card = cardModel,
            texture = TextureRegion(texture)
        )
    }

}
