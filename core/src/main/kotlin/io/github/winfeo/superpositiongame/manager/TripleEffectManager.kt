package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.dice.DiceActor
import io.github.winfeo.superpositiongame.ui.CardAndDiceContainer

//Поиск трёх дайсов для изменения на игровом поле
object TripleEffectManager {
    fun findDices (diceSlot: DiceActor): List<DiceActor> {
        val diceList = mutableListOf<DiceActor>()
        diceList.add(diceSlot)

        ///TODO переделать, хранить информацию о родительской таблице в слотах?
        val container = diceSlot.parent.parent as CardAndDiceContainer
        val parentTableChildren = (container.parent as Table).children
        val index = parentTableChildren.indexOf(container)

        val previousContainer = parentTableChildren.get(index - 1) as CardAndDiceContainer
        val previousDiceActor = previousContainer.diceSlot.getDiceActor()
        diceList.add(previousDiceActor)

        val nextContainer = parentTableChildren.get(index + 1) as CardAndDiceContainer
        val nextDiceActor = nextContainer.diceSlot.getDiceActor()
        diceList.add(nextDiceActor)

        return diceList
    }
}
