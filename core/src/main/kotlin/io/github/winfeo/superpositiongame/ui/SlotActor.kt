package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.configs.GameConfig
import io.github.winfeo.superpositiongame.graphics.BorderTexture
import io.github.winfeo.superpositiongame.ui.screens.GameScreen
import io.github.winfeo.superpositiongame.utils.GraphicsUtils

// Класс-ячейка таблицы для помещения карты на игровое поле
class SlotActor(): Table() {
    ///TODO может быть сделать фабрику объектов? Чтобы каждый раз не тратить ресурсы на каждй новый объект
    private val cardWidth = GameConfig.cardWidth
    private val cardHeight = GameConfig.cardHeight

    private var state = SlotActorStates.NO_ACTION
    private var borderColor = Color.GOLD

    ///TODO подумать, может динамически параметр рассчитывать как-то от размеров
    val bordersThickness = 2f
    val cornerRadius = 5f

    init {
        defaults()
            .minSize(cardWidth, cardHeight)
            .prefSize(cardWidth, cardHeight)
            .maxSize(cardWidth,cardHeight)
        pad(5f) //отступ от границы
    }

    fun setState(newState: SlotActorStates) {
        state = newState
        borderColor = when (state) {
            SlotActorStates.NO_ACTION -> Color.GOLD
            SlotActorStates.HOVERED_CAN_PLACE -> Color.GREEN
            SlotActorStates.HOVERED_CANT_PLACE -> Color.RED
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        val texture = BorderTexture.getBorderTexture(
            width,
            height,
            cornerRadius,
            bordersThickness
        )

        val oldColor = batch.color
        batch.color = Color(borderColor).apply { a *= parentAlpha }
        batch.draw(texture, x, y, width, height)
        batch.color = oldColor
    }

    fun placeCard(card: CardActor) {
        //currentCard = card
        children.filterIsInstance<Image>().forEach { it.remove() }
        clearChildren()
        add(card).size(card.width, card.height).center()
    }

}
