package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.dice.DiceActorBuilder
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.config.GameConfig

//Создание структуры слотов для отображения кубитов
///TODO переписать с использованием контейнера для одного слота и одного кубита - пара?
class GameTableDice(cardTable: GameTableCard): Table() {
    private val gameCardTable: GameTableCard = cardTable
    private val playerDiceSlots = mutableListOf<SlotActor>()
    private val opponentDiceSlots = mutableListOf<SlotActor>()

    init {
        createLayouts()
        Gdx.app.postRunnable {
            positionSlots()
            dealDiceSides() ///TODO сейчас просто случайн сторон, а не в соответ с карт задания
        }

        //debugAll()
    }

    private fun createLayouts() {
        repeat(GameConfig.getCardsOnTableAmount()) {
            val diceSlotPlayer = SlotActor()
            val diceSlotOpponent = SlotActor()
            playerDiceSlots.add(diceSlotPlayer)
            opponentDiceSlots.add(diceSlotOpponent)
            addActor(diceSlotPlayer)
            addActor(diceSlotOpponent)
        }
    }

    private fun positionSlots() {
        ///TODO хардкодятся, сделать динамически вместе со слотами карт
        val playerCardSlotPositions: List<Vector2> = gameCardTable.getPlayerSlotPositions()
        playerCardSlotPositions.forEachIndexed { index, cardSlot ->
            val diceSlot = playerDiceSlots[index]
            diceSlot.setPosition(
                cardSlot.x - diceSlot.width / 2,
                cardSlot.y + GameConfig.cardHeight - diceSlot.height / 2
            )
        }

        val opponentCardSlotPositions: List<Vector2> = gameCardTable.getOpponentSlotPositions()
        opponentCardSlotPositions.forEachIndexed { index, cardSlot ->
            val diceSlot = opponentDiceSlots[index]
            diceSlot.setPosition(
                cardSlot.x - diceSlot.width / 2,
                cardSlot.y + GameConfig.cardHeight - diceSlot.height / 2
            )
        }
    }

    private fun dealDiceSides() {
        playerDiceSlots.forEach { slot ->
            val diceSide = DiceActorBuilder.createRandomDice()
            slot.placeDice(diceSide)
        }

        opponentDiceSlots.forEach { slot ->
            val diceSide = DiceActorBuilder.createRandomDice()
            slot.placeDice(diceSide)
        }
    }
}
