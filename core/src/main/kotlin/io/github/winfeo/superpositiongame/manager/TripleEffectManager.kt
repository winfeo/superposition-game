package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.dice.DiceActor
import io.github.winfeo.superpositiongame.ui.CardAndDiceContainer

//Поиск трёх дайсов для изменения на игровом поле
object TripleEffectManager {
    fun findDices (diceSlot: DiceActor): Map<SlotActor, DiceActor> /*List<DiceActor>*/ {
        //val diceList = mutableListOf<DiceActor>()
        val cardAndDiceList = mutableMapOf<SlotActor, DiceActor>()
        //diceList.add(diceSlot)

        ///TODO переделать, хранить информацию о родительской таблице в слотах?
        val container = diceSlot.parent.parent as CardAndDiceContainer
        val currentCardSlot = container.cardSlot
        cardAndDiceList.put(currentCardSlot, diceSlot)

        val parentTableChildren = (container.parent as Table).children
        val index = parentTableChildren.indexOf(container)

        val previousContainer = parentTableChildren.get(index - 1) as CardAndDiceContainer
        val previousDiceActor = previousContainer.diceSlot.getDiceActor()
        val previousCardSlot = previousContainer.cardSlot
        //diceList.add(previousDiceActor)
        cardAndDiceList.put(previousCardSlot, previousDiceActor)


        val nextContainer = parentTableChildren.get(index + 1) as CardAndDiceContainer
        val nextDiceActor = nextContainer.diceSlot.getDiceActor()
        val nextCardSlot = nextContainer.cardSlot
        //diceList.add(nextDiceActor)
        cardAndDiceList.put(nextCardSlot, nextDiceActor)

        return cardAndDiceList
    }
}
