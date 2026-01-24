package io.github.winfeo.superpositiongame.managers.dragAndDrop.validation

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.winfeo.superpositiongame.actors.CardActor
import io.github.winfeo.superpositiongame.actors.SlotActor
import io.github.winfeo.superpositiongame.actors.SlotActorStates
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.CardDragPayload
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.ValidationResult
import io.github.winfeo.superpositiongame.managers.dragAndDrop.interfaces.DropValidation

//валидатор для слотов
class SlotValidation: DropValidation {
    ///TODO вынести все строки в отдельный файл? В том числе для локализации потом
    override fun canAccept(
        payload: CardDragPayload,
        target: Actor
    ): ValidationResult {
//        val slot = target as? SlotActor
//            ?: return ValidationResult(canPlace = false,
//                message = "Отладка. Карта не в слоте",
//                activeColor = Color.RED
//            )
//
//        val card = payload.sourceActor as? CardActor
//            ?: return ValidationResult(
//                canPlace = false,
//                message = "Отладка. Выбрана не карта",
//                activeColor = Color.RED
//            )

        ///TODO добавить сюда правила игры, работа с картами, проверка состояния кубита
        //if (!slot.isEmpty()) = return ValidationResult.invalid("В слоте уже есть карта!")

        return ValidationResult(
            canPlace = true,
            //activeColor = Color.GREEN,
            activeState = SlotActorStates.HOVERED_CAN_PLACE
            )

    }

    override fun onDrop(
        payload: CardDragPayload,
        target: Actor
    ) {
        val card = payload.sourceActor as CardActor
        val slot = target as SlotActor

        card.remove()
        slot.placeCard(card)
    }
}
