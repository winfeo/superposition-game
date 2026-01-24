package io.github.winfeo.superpositiongame.actor.dice

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import io.github.winfeo.superpositiongame.model.Dice

//Класс для отрисовки игрового кубита
class DiceActor(
    cardSide: Float,
    val dice: Dice,
    texture: TextureRegion
): Image(texture) {

    init {
        setSize(cardSide,cardSide)
    }

}
