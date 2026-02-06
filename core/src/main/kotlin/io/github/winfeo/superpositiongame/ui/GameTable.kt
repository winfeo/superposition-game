package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.SlotActorStates
import io.github.winfeo.superpositiongame.actor.SlotArea
import io.github.winfeo.superpositiongame.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.actor.dice.DiceActorBuilder
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameDragController
import io.github.winfeo.superpositiongame.manager.doubleTap.GameTapController
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.Dice

// Создание структуры слотов для отображения игральных карт
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTable(
    private val dragController: GameDragController,
    private val touchController: GameTapController
): Table() {
//    private val playerCardSlots = mutableListOf<SlotActor>()
    private val playerSlotContainers = mutableListOf<CardAndDiceContainer>()
    private val opponentSlotContainers = mutableListOf<CardAndDiceContainer>()
    private val cardsPadding = GameConfig.getCardsPadding()
    private val tablesPadding = GameConfig.getTablesPadding()

    //private val dragController = GameDragController()
    //private val touchController = GameTapController()

    init {
        setUpTable()
        createLayouts()

        //debugAll()
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
    }

    private fun createOpponentTaskArea(): Table {
        val opponentTaskArea = Table()
        opponentTaskArea.defaults().space(cardsPadding)

        repeat(GameConfig.getSlotsOnTableAmount()) {
            val container = CardAndDiceContainer(SlotArea.OPPONENT)
            opponentSlotContainers.add(container)
            opponentTaskArea.add(container)
        }

        return opponentTaskArea
    }

    private fun createPlayerTaskArea(): Table {
        val playerTaskArea = Table()
        playerTaskArea.defaults().space(cardsPadding)

        repeat(GameConfig.getSlotsOnTableAmount()) {
            val container = CardAndDiceContainer(SlotArea.PLAYER)
            playerSlotContainers.add(container)
            playerTaskArea.add(container)
        }

        return playerTaskArea
    }

    fun setUpTableActors(
        playerCards: List<Card>,
        playerDices: List<Dice>,
        opponentCards: List<Card>,
        opponentDices: List<Dice>
    ) {

        playerSlotContainers.forEachIndexed { index, container ->
            val cardActor = CardActorBuilder.createEmptyCardFromModel(playerCards.get(index))
            val diceActor = DiceActorBuilder.createRandomDice(playerDices.get(index))
            container.cardSlot.placeCard(cardActor)
            container.diceSlot.placeDice(diceActor)

            if (diceActor.dice.isInRequiredState()) {
                container.diceSlot.setState(SlotActorStates.REQUIRED_DICE_STATE)
            }
        }

        opponentSlotContainers.forEachIndexed { index, container ->
            val cardActor = CardActorBuilder.createEmptyCardFromModel(opponentCards.get(index))
            val diceActor = DiceActorBuilder.createRandomDice(opponentDices.get(index))
            container.cardSlot.placeCard(cardActor)
            container.diceSlot.placeDice(diceActor)

            if (diceActor.dice.isInRequiredState()) {
                container.diceSlot.setState(SlotActorStates.REQUIRED_DICE_STATE)
            }
        }

        setUpDropSlots()
    }

    fun setUpDropSlots() {
        playerSlotContainers.forEach { container ->
            dragController.setupSlot(container.cardSlot, "slot")
            ///TODO сделать типы валидаторов состояниями тоже
        }

        opponentSlotContainers.forEach { container ->
            dragController.setupSlot(container.cardSlot, "attack_slot")
        }
    }

    fun getPlayerDices(): List<Dice> {
        return playerSlotContainers.map { it.diceSlot.getDiceActor().dice }
    }

    fun getOpponentSlots(): List<CardAndDiceContainer> {
        return opponentSlotContainers
    }

    fun getPlayerSlots(): List<CardAndDiceContainer> {
        return playerSlotContainers
    }
}
