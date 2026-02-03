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

    fun createCardActorFromModel(card: Card): CardActor {
        val textureName = card.textureId
        val texture = CardsAtlasManager.getRegion(textureName)?: throw (IllegalStateException("Не удалось найти текстуру для карты: $textureName"))

        return CardActor(
            cardWidth = cardWidth,
            cardHeight = cardHeight,
            card = card,
            canDrag = card.canDrag,
            texture = texture
        )

    }

    ///TODO не создавать объект карт для пустых слотов, а просто рамку по размеру отрисовывавть?
    //Получится ли тогда драг анд дроп реализовать?
    fun createEmptyCardFromModel(card: Card): CardActor {
        val pixmap = Pixmap(70, 120, Pixmap.Format.RGBA8888) ///TODO статический размер?
        pixmap.setColor(Color.CLEAR)
        pixmap.fill()
        val texture = Texture(pixmap)
        pixmap.dispose()

        return CardActor(
            cardWidth = cardWidth,
            cardHeight = cardHeight,
            card = card,
            texture = TextureRegion(texture)
        )
    }

    fun createEmptyCard(): CardActor {
        val cardModel = Card(
            id = "empty",
            type = CardType.EMPTY
        )

        val pixmap = Pixmap(70, 120, Pixmap.Format.RGBA8888) ///TODO статический размер?
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
