package io.github.winfeo.superpositiongame.managers.dragAndDrop.validation

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.winfeo.superpositiongame.actors.CardActor
import io.github.winfeo.superpositiongame.actors.SlotActorStates
import io.github.winfeo.superpositiongame.managers.dragAndDrop.GameAreas
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.CardDragData
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.CardDragPayload
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.ValidationResult
import io.github.winfeo.superpositiongame.managers.dragAndDrop.interfaces.DropValidation

///TODO добавить методы для валидации при раздаче карт
class HandCardValidation: DropValidation {
    override fun canAccept(
        payload: CardDragPayload,
        target: Actor
    ): ValidationResult {
//        val card = payload.sourceActor as? CardActor
//            ?: return ValidationResult(
//                canPlace = false,
//                message = "Отладка. Не карта",
//                activeColor = Color.RED
//            )

        val dragData = payload.data as? CardDragData
            ?: return ValidationResult(
                canPlace = false,
                message = "Отладка. Нет инфо карты",
                //activeColor = Color.RED,
                activeState = SlotActorStates.HOVERED_CANT_PLACE

            )

        return if (dragData.sourceArea == GameAreas.PLAYER_HAND) {
            ValidationResult(canPlace = true)
        } else {
            ValidationResult(
                canPlace = false,
                message = "Отладка. Можно перетаск только с руки",
                //activeColor = Color.RED,
                activeState = SlotActorStates.HOVERED_CANT_PLACE
            )
        }
    }

    override fun onDrop(
        payload: CardDragPayload,
        target: Actor
    ) {

    }
}
