package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import io.github.winfeo.superpositiongame.configs.GameConfig
import io.github.winfeo.superpositiongame.models.Card

// Класс конкретной карты (на руках игрока) для отображения карты
class CardActor(
    val cardWidth: Float,
    val cardHeight: Float,
    val card: Card,
    val texture: TextureRegion
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
