package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.SlotArea
import io.github.winfeo.superpositiongame.config.GameConfig

//Класс ячейки игровго поля (кубик + карта слоты)
class CardAndDiceContainer(
    area: SlotArea
): Table() {
    val cardSlot: SlotActor
    val diceSlot: SlotActor

    private val cardWidth = GameConfig.cardWidth
    private val cardHeight = GameConfig.cardHeight
    private val diceSide = GameConfig.getDiceSide()

    init {
        cardSlot = SlotActor(area).apply {
            setSize(cardWidth, cardHeight)
        }
        add(cardSlot)

        diceSlot = SlotActor(SlotArea.DICE).apply {
            ///TODO добавить рамку вокруг ячеек с кубитами? Сделать больше размер?
            setSize(diceSide + 7f, diceSide + 7f)
            setPosition(
                -cardSlot.width / 4,
                cardSlot.height - height / 4
            )
        }
        addActor(diceSlot)

        setSize(cardWidth, cardHeight)
    }
}
