package io.github.winfeo.superpositiongame.actor.dice

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.manager.DiceAtlasManager
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.model.dice.DiceState

//Класс для отрисовки игрового кубита
class DiceActor(
    cardSide: Float,
    var dice: Dice,
    texture: TextureRegion,
    private var touchable: Touchable = Touchable.disabled,
    private var previousDice: Dice
): Image(texture) {
    //private var isSelectedForSwap = false

    init {
        setSize(cardSide,cardSide)
    }

    fun changeState(newState: DiceState) {
        previousDice = dice
        dice = dice.copy(state = newState)
        val newTexture = DiceAtlasManager.getStateTexture(newState.textureId)
        drawable = TextureRegionDrawable(newTexture)
    }

    fun enableTouchableEffect(){
        touchable = Touchable.enabled
    }

    fun disableTouchableEffect() {
        touchable = Touchable.disabled
    }

    fun setPreviousMoveDiceType() {
        changeState(previousDice.state)
    }

    fun setPreviousMoveDice() {
        previousDice = dice
    }

    fun getPreviousMoveDice(): Dice {
        return previousDice
    }

}
