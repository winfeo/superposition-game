package io.github.winfeo.superpositiongame.manager.dragAndDrop.interfaces

import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.winfeo.superpositiongame.manager.dragAndDrop.data.CardDragPayload
import io.github.winfeo.superpositiongame.manager.dragAndDrop.data.ValidationResult

//проверка, можно ли поместить объект в слот
interface DropValidation {
    fun canAccept(payload: CardDragPayload, target: Actor): ValidationResult
    fun onDrop(payload: CardDragPayload, target: Actor)
}
