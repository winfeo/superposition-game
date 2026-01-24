package io.github.winfeo.superpositiongame.manager.dragAndDrop.data

import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameAreas
import io.github.winfeo.superpositiongame.model.Card

data class CardDragData (
    val card: Card,
    val sourceArea: GameAreas
)
