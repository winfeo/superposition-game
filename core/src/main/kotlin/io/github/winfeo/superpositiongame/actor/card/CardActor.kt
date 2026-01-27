package io.github.winfeo.superpositiongame.actor.card

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import io.github.winfeo.superpositiongame.model.Card

//Класс для отрисовки игровой карты
class CardActor(
    cardWidth: Float,
    cardHeight: Float,
    val card: Card,
    var canDrag: Boolean = false,
    texture: TextureRegion
): Image(texture) {

    init {
//        val cardWidth = texture.regionWidth.toFloat()
//        val cardHeight = texture.regionHeight.toFloat()
//
//        val screenWidth = Gdx.graphics.width.toFloat()
//        val screenHeight = Gdx.graphics.height.toFloat()
//
//        val newWidth = screenWidth / cardWidth
//        val newHeight = screenWidth / cardWidth

        //setSize(70f,120f)
//        val width = Gdx.graphics.width.toFloat() * GameConfig.CARD_WIDTH_PERCENT
//        val height = Gdx.graphics.height.toFloat() * GameConfig.CARD_HEIGHT_RATIO
        setSize(cardWidth,cardHeight)
    }


}
