package io.github.winfeo.superpositiongame.managers.dragAndDrop.data

import io.github.winfeo.superpositiongame.managers.dragAndDrop.GameAreas
import io.github.winfeo.superpositiongame.models.Card

data class CardDragData (
    val card: Card,
    val sourceArea: GameAreas
)
