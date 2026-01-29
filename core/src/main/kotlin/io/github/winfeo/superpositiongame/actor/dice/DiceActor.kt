package io.github.winfeo.superpositiongame.actor.dice

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.manager.DiceAtlasManager
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.model.dice.DiceState

//Класс для отрисовки игрового кубита
class DiceActor(
    cardSide: Float,
    var dice: Dice,
    texture: TextureRegion
): Image(texture) {

    init {
        setSize(cardSide,cardSide)
    }

    fun changeState(newState: DiceState) {
        dice = dice.copy(state = newState)
        val newTexture = DiceAtlasManager.getStateTexture(newState.textureId)
        drawable = TextureRegionDrawable(newTexture)
    }

}
