package io.github.winfeo.superpositiongame.ui.actor.card

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.manager.CardsAtlasManager
import io.github.winfeo.superpositiongame.model.card.Card

//Отрисовка игровой карты
class CardActor(
    cardWidth: Float,
    cardHeight: Float,
    var card: Card,
    var canDrag: Boolean = false,
//    private var touchable: Touchable = Touchable.enabled,
): Image(
    CardsAtlasManager.getRegion(card.textureId!!)
) {

    init {
        setSize(cardWidth,cardHeight)
    }

    fun render(newCard: Card) {
        card = newCard
        drawable = TextureRegionDrawable(CardsAtlasManager.getRegion(card.textureId!!))
    }

}
