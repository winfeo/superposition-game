package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.configs.GameConfig

// Класс для создания структуры игрового поля
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTable(): Table() {
    private var playerCardSlots = mutableListOf<SlotActor>()
    private var opponentTaskSlots = mutableListOf<SlotActor>()
    private var playerTaskSlots = mutableListOf<SlotActor>()

    private val cardsPadding = GameConfig.getCardsPadding()
    private val tablesPadding = GameConfig.getTablesPadding()

    init {
        setUpTable()
        createLayouts()
        ///TODO Создать отдельный класс для упралвения действиями в игре
        dealCards()
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

        repeat(GameConfig.getCardsInHandAmount()) {
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
