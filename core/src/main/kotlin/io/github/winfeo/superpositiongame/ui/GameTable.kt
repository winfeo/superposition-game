package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table

// Класс для создания структуры игрового поля
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTable(val tableRows: Int = 1, val tableCols: Int = 4): Table() {
    private val opponentSlots = mutableListOf<SlotActor>()
    private val playerSlots = mutableListOf<SlotActor>()

    init {
        setUpTable()
        createLayouts()
        ///TODO Создать отдельный класс для упралвения действиями в игре
        dealCards()
    }

    private fun setUpTable() {
        setFillParent(true)
        pad(20f)
        defaults().pad(5f) //настройки для всех ячеек
    }

    private fun createLayouts() {
        add(createOpponentCardsArea())
            .fillX()
            .row()
        add(createOpponentTaskArea())
            .height(80f)
            .fillX()
            .row()
        add(createPlayerTaskArea())
            .height(80f)
            .fillX()
            .row()
        add(createPlayerCardsArea())
            .fillX()
    }

    private fun createOpponentCardsArea(): Table {
        val opponentCardsArea = Table()
        opponentSlots.clear()

        /// TODO пока у каждого игрока на руках будет по 6 карт
        for (i in 0..<6) {
            val cardSlot = SlotActor(col = i)
            opponentSlots.add(cardSlot)
            opponentCardsArea.add(cardSlot).size(cardSlot.width, cardSlot.height)
        }

        return opponentCardsArea
    }

    private fun createOpponentTaskArea(): Table {
        val opponentTastArea = Table()

        //создание ячеек таблицы
        for (rowLine in 0..<tableRows) {
            for (colLine in 0..<tableCols){
                val cardSlot = SlotActor(row = rowLine, col = colLine)
                opponentTastArea.add(cardSlot)
            }
            opponentTastArea.row()
        }

        return opponentTastArea
    }

    private fun createPlayerCardsArea(): Table {
        val playerCardsArea = Table()
        playerSlots.clear()

        /// TODO пока у каждого игрока на руках будет по 6 карт
        for (i in 0..<6) {
            val cardSlot = SlotActor(col = i)
            playerSlots.add(cardSlot)
            playerCardsArea.add(cardSlot).size(cardSlot.width, cardSlot.height)
        }

        return playerCardsArea
    }

    private fun createPlayerTaskArea(): Table {
        val playerTaskArea = Table()

        //создание ячеек таблицы
        for (rowLine in 0..<tableRows) {
            for (colLine in 0..<tableCols){
                val cardSlot = SlotActor(row = rowLine, col = colLine)
                playerTaskArea.add(cardSlot)
            }
            playerTaskArea.row()
        }

        return playerTaskArea
    }

    private fun dealCards() {
        playerSlots.forEach { slot ->
            val card = CardActorBuilder.createRandomCard()
            slot.placeCard(card)
        }

        opponentSlots.forEach { slot ->
            val card = CardActorBuilder.createRandomCard()
            slot.placeCard(card)
        }
    }
}
