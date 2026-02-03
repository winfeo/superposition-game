package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.actor.dice.DiceActorBuilder
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameDragController
import io.github.winfeo.superpositiongame.manager.doubleTap.GameTapController
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.Dice

// Создание структуры слотов для отображения игральных карт
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTable(): Table() {
    private val playerCardSlots = mutableListOf<SlotActor>()
    private val playerSlotContainer = mutableListOf<CardAndDiceContainer>()
    private val opponentSlotContainer = mutableListOf<CardAndDiceContainer>()
    private val cardsPadding = GameConfig.getCardsPadding()
    private val tablesPadding = GameConfig.getTablesPadding()

    private val dragController = GameDragController()
    private val touchController = GameTapController()

    init {
        setUpTable()
        createLayouts()

        //debugAll()
    }
    fun setUpCardUsage() {
        playerCardSlots.forEach { slot ->
            slot.getCardActor()?.let { card ->
                if (card.canDrag) dragController.setupCard(card)
                else touchController.setupCard(card)

            }
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

        repeat(GameConfig.getSlotsOnTableAmount()) {
            val container = CardAndDiceContainer()
            opponentSlotContainer.add(container)
            opponentTaskArea.add(container)
        }

        return opponentTaskArea
    }

    private fun createPlayerTaskArea(): Table {
        val playerTaskArea = Table()
        playerTaskArea.defaults().space(cardsPadding)

        repeat(GameConfig.getSlotsOnTableAmount()) {
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

    fun dealPlayerCards(cards: List<Card>) {
        playerCardSlots.forEachIndexed { index, slot ->
            val cardActor = CardActorBuilder.createCardActorFromModel(cards.get(index))
            slot.placeCard(cardActor)
        }
    }

    fun setUpTableActors(
        playerCards: List<Card>,
        playerDices: List<Dice>,
        opponentCards: List<Card>,
        opponentDices: List<Dice>
    ) {

        playerSlotContainer.forEachIndexed { index, container ->
            val cardActor = CardActorBuilder.createEmptyCardFromModel(playerCards.get(index))
            val diceActor = DiceActorBuilder.createRandomDice(playerDices.get(index))
            container.cardSlot.placeCard(cardActor)
            container.diceSlot.placeDice(diceActor)
        }

        opponentSlotContainer.forEachIndexed { index, container ->
            val cardActor = CardActorBuilder.createEmptyCardFromModel(opponentCards.get(index))
            val diceActor = DiceActorBuilder.createRandomDice(opponentDices.get(index))
            container.cardSlot.placeCard(cardActor)
            container.diceSlot.placeDice(diceActor)
        }

        setUpDropSlots()
    }

    fun setUpDropSlots() {
        playerSlotContainer.forEach { container ->
            dragController.setupSlot(container.cardSlot, "slot")
            ///TODO сделать типы валидаторов состояниями тоже
        }

        opponentSlotContainer.forEach { container ->
            dragController.setupSlot(container.cardSlot, "attack_slot")
        }
    }
}
