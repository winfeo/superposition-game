package io.github.winfeo.superpositiongame.model.card

import io.github.winfeo.superpositiongame.model.card.components.CardComponent

//Entity?
data class Card(
    val id: String,
    val type: CardType
    ///TODO добавить порядковую позицию кубита в общем ряде (нужно для Rotate-Gate и x-3 гейтов)?
) {
    val component: CardComponent = type.cardComponent
    val canPlace: Boolean = component.canMove

    val name: String = type.cardName
    val textureId: String = type.textureId
    val actionRadius: Int = component.actionRadius
    val requireSpecialSlots: Boolean = component.requiredSpecialSlot
    val isMovable: Boolean = component.canMove
}
