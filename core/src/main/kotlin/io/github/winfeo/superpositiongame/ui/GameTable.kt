package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actors.CardActor
import io.github.winfeo.superpositiongame.actors.CardActorBuilder
import io.github.winfeo.superpositiongame.actors.SlotActor
import io.github.winfeo.superpositiongame.configs.GameConfig
import io.github.winfeo.superpositiongame.managers.dragAndDrop.GameAreas
import io.github.winfeo.superpositiongame.managers.dragAndDrop.GameDragController

// Класс для создания структуры игрового поля
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTable(): Table() {
    private var playerCardSlots = mutableListOf<SlotActor>()
    private var opponentTaskSlots = mutableListOf<SlotActor>()
    private var playerTaskSlots = mutableListOf<SlotActor>()

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

        //testBasicCard()
    }

    private fun testBasicCard() {
        println("=== НАЧАЛО ТЕСТА ===")

        // 1. Создаем одну тестовую карту
        val testCard = CardActorBuilder.createRandomCard()
        println("Создана тестовая карта: ${testCard.width}x${testCard.height}")

        // 2. Создаем один тестовый слот
        val testSlot = SlotActor()
        println("Создан тестовый слот")

        // 3. Добавляем карту в слот
        testSlot.placeCard(testCard)

        // 4. Добавляем слот в GameTable
        add(testSlot)
            .size(testSlot.prefWidth, testSlot.prefHeight)
            .row()

        // 5. Проверяем состояние
        println("Проверка состояния:")
        println("- GameTable детей: ${children.size}")
        println("- Slot детей: ${testSlot.children.size}")
        println("- Card размер: ${testCard.width}x${testCard.height}")
        println("- Card touchable: ${testCard.isTouchable}")
        println("- Card видима: ${testCard.isVisible}")

        println("=== КОНЕЦ ТЕСТА ===")
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
}
