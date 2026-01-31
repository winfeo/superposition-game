package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.dice.DiceActorBuilder
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameDragController

// Создание структуры слотов для отображения игральных карт
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTableCard(
    stage: Stage
): Table() {
    private val playerCardSlots = mutableListOf<SlotActor>()
    private val playerSlotContainer = mutableListOf<CardAndDiceContainer>()
    private val opponentSlotContainer = mutableListOf<CardAndDiceContainer>()
    private val cardsPadding = GameConfig.getCardsPadding()
    private val tablesPadding = GameConfig.getTablesPadding()

    private val dragController = GameDragController(stage)

    init {
        setUpTable()
        createLayouts()
        ///TODO Создать отдельный класс для упралвения действиями в игре
        dealCards()
        dealDiceSides()
        setUpDragAndDrop()

        //debugAll()
    }

    private fun setUpDragAndDrop() {
        playerCardSlots.forEach { slot ->
            slot.getCard().let { card ->
                dragController.setupCard(card)
            }
        }

        playerSlotContainer.forEach { container ->
            dragController.setupSlot(container.cardSlot, "slot")
            ///TODO сделать типы валидаторов состояниями тоже
        }

        opponentSlotContainer.forEach { container ->
            dragController.setupSlot(container.cardSlot, "attack_slot")
        }
    }

    private fun setUpTable() {
        setFillParent(true)
        defaults().pad(tablesPadding.also { println("TablePadding: $it") }) //расс-ние между рядами
    }

    private fun createLayouts() {
        ///TODO сделать не через добавление add на сцену, а через добавление доп актора?
        add(createOpponentTaskArea())
            .fillX()
            .row()
        add(createPlayerTaskArea())
            .fillX()
            .row()
        add(createPlayerCardsArea())
            .fillX()
    }

    private fun createOpponentTaskArea(): Table {
        val opponentTaskArea = Table()
        opponentTaskArea.defaults().space(cardsPadding)

        repeat(GameConfig.getCardsOnTableAmount()) {
            val container = CardAndDiceContainer()
            opponentSlotContainer.add(container)
            opponentTaskArea.add(container)
        }

        return opponentTaskArea
    }

    private fun createPlayerTaskArea(): Table {
        val playerTaskArea = Table()
        playerTaskArea.defaults().space(cardsPadding)

        repeat(GameConfig.getCardsOnTableAmount()) {
            val container = CardAndDiceContainer()
            playerSlotContainer.add(container)
            playerTaskArea.add(container)
        }

        return playerTaskArea
    }

    private fun createPlayerCardsArea(): Table {
        val playerCardsArea = Table()
        playerCardSlots.clear()
        playerCardsArea.defaults().space(cardsPadding)

        repeat(GameConfig.getCardsInHandAmount()) {
            val cardSlot = SlotActor()
            playerCardSlots.add(cardSlot)
            playerCardsArea.add(cardSlot)
        }

        return playerCardsArea
    }

    private fun dealCards() {
        playerCardSlots.forEach { slot ->
            val card = CardActorBuilder.createRandomCard()
            slot.placeCard(card)
        }

        playerSlotContainer.forEach { slot ->
            val card = CardActorBuilder.createEmptyCard()
            slot.cardSlot.placeCard(card)
        }

        opponentSlotContainer.forEach { slot ->
            val card = CardActorBuilder.createEmptyCard()
            slot.cardSlot.placeCard(card)
        }

    }

    private fun dealDiceSides() {
        playerSlotContainer.forEach { container ->
            val dice = DiceActorBuilder.createRandomDice()
            container.diceSlot.placeDice(dice)
        }

        opponentSlotContainer.forEach { container ->
            val dice = DiceActorBuilder.createRandomDice()
            container.diceSlot.placeDice(dice)
        }
    }
}
