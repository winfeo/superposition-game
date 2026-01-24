package io.github.winfeo.superpositiongame.manager.dragAndDrop.validation

import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.SlotActorStates
import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameAreas
import io.github.winfeo.superpositiongame.manager.dragAndDrop.data.CardDragData
import io.github.winfeo.superpositiongame.manager.dragAndDrop.data.CardDragPayload
import io.github.winfeo.superpositiongame.manager.dragAndDrop.data.ValidationResult
import io.github.winfeo.superpositiongame.manager.dragAndDrop.interfaces.DropValidation

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
                //activeColor = Color.RED
                activeState = SlotActorStates.HOVERED_CANT_PLACE
            )

        val dragData = payload.data as? CardDragData
            ?: return ValidationResult(
                canPlace = false,
                message = "Отладка. Нет данных для пер-ния",
                //activeColor = Color.RED
                activeState = SlotActorStates.HOVERED_CANT_PLACE
            )

        return if (canAttack(card, dragData.sourceArea)) {
            ValidationResult(
                canPlace = true,
                //activeColor = Color.GREEN,
                activeState = SlotActorStates.HOVERED_CAN_PLACE)
        } else {
            ValidationResult(
                canPlace = false,
                message = "Отладка. Нелья исп эту карту",
                //activeColor = Color.RED
                activeState = SlotActorStates.HOVERED_CANT_PLACE

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
