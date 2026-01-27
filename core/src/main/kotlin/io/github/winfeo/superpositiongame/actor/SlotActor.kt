package io.github.winfeo.superpositiongame.actor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.dice.DiceActor
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.graphics.BorderTexture
import ktx.collections.isNotEmpty
import kotlin.math.sin

// Класс-ячейка таблицы для помещения карты на игровое поле
class SlotActor(): Table() {
    ///TODO может быть сделать фабрику объектов? Чтобы каждый раз не тратить ресурсы на каждй новый объект
    private var state = SlotActorStates.NO_ACTION
    private var borderColor = Color.GOLD
    private var borderTexture = BorderTexture.getBorderTexture()

    private var pulseTime = 0f
    private val pulseSpeed = 8f

    init {
        defaults()
        //touchable = Touchable.enabled
        pad(5f) //отступ от границы
    }

    fun setState(newState: SlotActorStates) {
        state = newState
        borderColor = when (state) {
            SlotActorStates.NO_ACTION -> Color.GOLD
            SlotActorStates.HOVERED_CAN_PLACE -> Color.CYAN
            SlotActorStates.HOVERED_CANT_PLACE -> Color.RED
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (state != SlotActorStates.NO_ACTION) pulseTime += delta * pulseSpeed
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        val oldColor = batch.color
        val newColor = Color(borderColor)
        if (state != SlotActorStates.NO_ACTION) {
            val pulseAlpha = 0.7f + 0.3f * sin(pulseTime)
            newColor.a = pulseAlpha * parentAlpha
        }
        else {
            newColor.a = borderColor.a * parentAlpha
        }

        batch.color = newColor
        batch.draw(borderTexture, x, y, width, height)
        batch.color = oldColor
    }

    fun placeCard(card: CardActor) {
        children.filterIsInstance<Image>().forEach { it.remove() }
        clearChildren()
        println("Card placed: ${card.width}x${card.height}, touchable: ${card.isTouchable}")
        add(card).size(card.width, card.height).center()
        card.touchable = Touchable.enabled
    }

    fun placeDice(dice: DiceActor) {
        children.filterIsInstance<Image>().forEach { it.remove() }
        clearChildren()
        add(dice).size(dice.width, dice.height).center()
        dice.touchable = Touchable.disabled
        println("Кубит создан: ${dice.width}x${dice.height}, touchable: ${dice.isTouchable}")
    }
    fun getCard(): CardActor = children.first() as CardActor

}
