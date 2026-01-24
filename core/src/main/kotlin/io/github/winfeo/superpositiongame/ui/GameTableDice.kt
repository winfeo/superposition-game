package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actors.SlotActor

//Создание структуры слотов для отображения кубитов
class GameTableDice(): Table() {
    private val playerDiceSlots = mutableListOf<SlotActor>()
    private val opponentDiceSlots = mutableListOf<SlotActor>()

    init {
        createLayouts()
        dealDiceSides() ///TODO сейчас просто случайн сторон, а не в соответ с карт задания
    }

    private fun createLayouts() {
        add(createOpponentDiceArea())
        add(createPlayerDiceArea())
    }

    private fun createOpponentDiceArea(): Table {

    }

    private fun createPlayerDiceArea(): Table {

    }

    private fun dealDiceSides() {

    }
}
