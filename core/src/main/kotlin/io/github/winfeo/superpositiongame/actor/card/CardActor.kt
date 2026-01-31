package io.github.winfeo.superpositiongame.actor.card

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import io.github.winfeo.superpositiongame.model.card.Card

//Класс для отрисовки игровой карты
class CardActor(
    cardWidth: Float,
    cardHeight: Float,
    val card: Card,
    var canDrag: Boolean = false,
    texture: TextureRegion,
    private var touchable: Touchable = Touchable.enabled
): Image(texture) {

    init {
        setSize(cardWidth,cardHeight)
    }
}
