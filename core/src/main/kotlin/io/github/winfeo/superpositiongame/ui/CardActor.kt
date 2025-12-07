package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import io.github.winfeo.superpositiongame.models.Card

// Класс конкретной карты (на руках игрока) для отображения карты
class CardActor(
    val card: Card,
    val texture: TextureRegion
): Image(texture) {

    init {
        setSize(70f,120f)
    }


}
