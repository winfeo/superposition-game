package io.github.winfeo.superpositiongame.actor.card

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.CardsAtlasManager
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType

object CardActorBuilder {
    private val cardWidth = GameConfig.cardWidth
    private val cardHeight = GameConfig.cardHeight
    private var idCounter: Int = 0

    fun createRandomCard(): CardActor {
        val textureName = CardsAtlasManager.getRandomCardId()
//        val textureName = "hadamard_h3"
        val cardType: CardType = CardType.entries.find { it.textureId == textureName }?: throw (IllegalStateException("Не удалось найти тип карты c id: $textureName"))

        val cardModel = Card(
            id = "${textureName}-$idCounter",
            type = cardType
        )

        val texture = CardsAtlasManager.getRegion(textureName)?: throw (IllegalStateException("Не удалось найти текстуру для карты: $textureName"))
        idCounter++
        return CardActor(
            cardWidth = cardWidth,
            cardHeight = cardHeight,
            card = cardModel,
            canDrag = cardModel.canPlace,
            texture = texture
        )

    }

    ///TODO не создавать объект карт для пустых слотов, а просто рамку по размеру отрисовывавть?
    //Получится ли тогда драг анд дроп реализовать?
    fun createEmptyCard(): CardActor {
        val cardModel = Card(
            id = "empty",
            type = CardType.EMPTY
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
