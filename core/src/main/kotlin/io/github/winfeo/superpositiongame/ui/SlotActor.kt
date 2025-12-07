package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table

// Класс-ячейка для помещения карты на игровое поле
// Передаются координаты (позиция) карты в конкретном слоте таблицы
class SlotActor(val row: Int = 0, val col: Int = 0): Table() {
    private var currentCard: CardActor? = null
    private val background: Image = Image().apply { color = Color.DARK_GRAY }

    init {
        setSize(80f, 130f)
        background.setSize(width, height)
        addActor(background)
        debug = true
    }

    fun placeCard(card: CardActor) {
        currentCard = card
        add(card).size(card.width, card.height).center()
    }
}
