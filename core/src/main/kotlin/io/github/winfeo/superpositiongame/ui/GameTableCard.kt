package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameAreas
import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameDragController

// Создание структуры слотов для отображения игральных карт
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTableCard(): Table() {
    private val playerCardSlots = mutableListOf<SlotActor>()
    private val opponentTaskSlots = mutableListOf<SlotActor>()
    private val playerTaskSlots = mutableListOf<SlotActor>()

    private val cardsPadding = GameConfig.getCardsPadding()
    private val tablesPadding = GameConfig.getTablesPadding()

    private val dragController = GameDragController()

    init {
        setUpTable()
        createLayouts()
        ///TODO Создать отдельный класс для упралвения действиями в игре
        dealCards()
        setUpDragAndDrop()

        //debugAll()
    }

    private fun setUpDragAndDrop() {
        // 1. Карты в руке игрока
        playerCardSlots.forEachIndexed { index, slot ->
            slot.getCard()?.let { card ->
                dragController.setupCard(card, GameAreas.PLAYER_HAND)
            }
        }

        // 2. Слоты на столе игрока (обычные)
        playerTaskSlots.forEach { slot ->
            dragController.setupSlot(slot, "slot")
            ///TODO сделать типы валидаторов состояниями тоже
        }

        // 3. Слоты на столе противника (для атак)
        opponentTaskSlots.forEach { slot ->
            dragController.setupSlot(slot, "attack_slot")
        }
    }

    private fun setUpTable() {
        setFillParent(true)
        defaults().pad(tablesPadding.also { println("TablePadding: $it") }) //расс-ние между рядами
    }

    //Создание таблиц (1 - Карты противника на столе, 2 - Карты игрока на столе, 3 - Карты игрока на руках)
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
            val cardSlot = SlotActor()
            opponentTaskSlots.add(cardSlot)
            opponentTaskArea.add(cardSlot)
        }

        return opponentTaskArea
    }

    private fun createPlayerTaskArea(): Table {
        val playerTaskArea = Table()
        playerTaskArea.defaults().space(cardsPadding)

        repeat(GameConfig.getCardsOnTableAmount()) {
            val cardSlot = SlotActor()
            playerTaskSlots.add(cardSlot)
            playerTaskArea.add(cardSlot)
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

        playerTaskSlots.forEach { slot ->
            val card = CardActorBuilder.createEmptyCard()
            slot.placeCard(card)
        }

        opponentTaskSlots.forEach { slot ->
            val card = CardActorBuilder.createEmptyCard()
            slot.placeCard(card)
        }

    }

    fun getPlayerSlots(): List<SlotActor> {
        return playerTaskSlots
    }

    fun getOpponentSlots(): List<SlotActor> {
        return opponentTaskSlots
    }
}
