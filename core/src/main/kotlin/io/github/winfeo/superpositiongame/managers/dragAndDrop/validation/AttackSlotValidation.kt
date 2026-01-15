package io.github.winfeo.superpositiongame.managers.dragAndDrop.validation

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.winfeo.superpositiongame.actors.CardActor
import io.github.winfeo.superpositiongame.actors.SlotActor
import io.github.winfeo.superpositiongame.managers.dragAndDrop.GameAreas
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.CardDragData
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.CardDragPayload
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.ValidationResult
import io.github.winfeo.superpositiongame.managers.dragAndDrop.interfaces.DropValidation

class AttackSlotValidation: DropValidation {

    override fun canAccept(payload: CardDragPayload, target: Actor): ValidationResult {
//        val slot = target as? SlotActor
//            ?: return ValidationResult(
//                canPlace = false,
//                message = "Отладка. Не слот",
//                activeColor = Color.RED
//            )

        val card = payload.sourceActor as? CardActor
            ?: return ValidationResult(
                canPlace = false,
                message = "Отладка. Не карта",
                activeColor = Color.RED
            )

        val dragData = payload.data as? CardDragData
            ?: return ValidationResult(
                canPlace = false,
                message = "Отладка. Нет данных для пер-ния",
                activeColor = Color.RED
            )

        return if (canAttack(card, dragData.sourceArea)) {
            ValidationResult(canPlace = true)
        } else {
            ValidationResult(
                canPlace = false,
                message = "Отладка. Нелья исп эту карту",
                activeColor = Color.RED
            )
        }
    }

    override fun onDrop(payload: CardDragPayload, target: Actor) {
        val card = payload.sourceActor as CardActor
        val slot = target as SlotActor

        card.remove()
        slot.placeCard(card)

        println("Отладка. Атака выполн")
    }

    private fun canAttack(card: CardActor, sourceArea: GameAreas): Boolean {
        return sourceArea == GameAreas.PLAYER_TABLE
    }
}
