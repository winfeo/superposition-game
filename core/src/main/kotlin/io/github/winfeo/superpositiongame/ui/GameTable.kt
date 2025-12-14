package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.configs.GameConfig
import ktx.graphics.color

// Класс для создания структуры игрового поля
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTable(
    val screenWidth: Float,
    val screenHeight: Float,
    val tableRows: Int = 1,
    val tableCols: Int = GameConfig.CARD_ON_TABLE
): Table() {
    private var playerCardSlots = mutableListOf<SlotActor>()
    private var opponentTaskSlots = mutableListOf<SlotActor>()
    private var playerTaskSlots = mutableListOf<SlotActor>()

    private val cardWidth = GameConfig.getCardWidth(screenWidth)
    private val cardHeight = GameConfig.getCardHeight(screenWidth)
    private val cardsPadding = GameConfig.getCardsPadding(screenWidth)
    private val tablesPadding = GameConfig.getTablesPadding(screenHeight)

    init {
        setUpTable()
        createLayouts()
        ///TODO Создать отдельный класс для упралвения действиями в игре
        dealCards()
    }

    private fun setUpTable() {
        setFillParent(true)
//        pad(20f)
//        color(0.2f, 0.2f, 0.3f, 0.8f)
        defaults().pad(tablesPadding.also { println("TablePadding: $it") }) //расс-ние между рядами
        //defaults().pad(20f)
    }

    private fun createLayouts() {
        add(createOpponentTaskArea())
//            .height(80f)
            .fillX()
            .row()
        add(createPlayerTaskArea())
//            .height(80f)
            .fillX()
            .row()
        add(createPlayerCardsArea())
            .fillX()
    }

    private fun createOpponentTaskArea(): Table {
        val opponentTaskArea = Table()
        opponentTaskArea.defaults().space(cardsPadding)

        //создание ячеек таблицы
        for (rowLine in 0..<tableRows) {
            for (colLine in 0..<tableCols){
                val cardSlot = SlotActor(
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    row = rowLine,
                    col = colLine)
                opponentTaskSlots.add(cardSlot)
                opponentTaskArea.add(cardSlot)
            }
            opponentTaskArea.row()
        }

        return opponentTaskArea
    }

    private fun createPlayerTaskArea(): Table {
        val playerTaskArea = Table()
        playerTaskArea.defaults().space(cardsPadding)

        //создание ячеек таблицы
        for (rowLine in 0..<tableRows) {
            for (colLine in 0..<tableCols){
                val cardSlot = SlotActor(
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    row = rowLine,
                    col = colLine)
                playerTaskSlots.add(cardSlot)
                playerTaskArea.add(cardSlot)
            }
            playerTaskArea.row()
        }

        return playerTaskArea
    }

    private fun createPlayerCardsArea(): Table {
        val playerCardsArea = Table()
        playerCardSlots.clear()
        playerCardsArea.defaults().space(cardsPadding)

        /// TODO пока у каждого игрока на руках будет по 6 карт
        for (i in 0..< GameConfig.CARDS_IN_HAND) {
            val cardSlot = SlotActor(
                cardWidth = cardWidth,
                cardHeight = cardHeight,
                col = i)
            playerCardSlots.add(cardSlot)
            playerCardsArea.add(cardSlot)//.size(cardSlot.width, cardSlot.height)
        }

        return playerCardsArea
    }

    private fun dealCards() {
        playerCardSlots.forEach { slot ->
            val card = CardActorBuilder.createRandomCard(cardWidth, cardHeight)
            slot.placeCard(card)
        }

        playerTaskSlots.forEach { slot ->
            val card = CardActorBuilder.createEmptyCard(cardWidth, cardHeight)
            slot.placeCard(card)
        }

        opponentTaskSlots.forEach { slot ->
            val card = CardActorBuilder.createEmptyCard(cardWidth, cardHeight)
            slot.placeCard(card)
        }

    }
}
