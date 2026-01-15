package io.github.winfeo.superpositiongame.managers.dragAndDrop.interfaces

import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.CardDragPayload
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.ValidationResult

//проверка, можно ли поместить объект в слот
interface DropValidation {
    fun canAccept(payload: CardDragPayload, target: Actor): ValidationResult
    fun onDrop(payload: CardDragPayload, target: Actor)
}
