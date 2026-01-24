package io.github.winfeo.superpositiongame.managers.dragAndDrop.data

import com.badlogic.gdx.graphics.Color
import io.github.winfeo.superpositiongame.actors.SlotActorStates

data class ValidationResult(
    val canPlace: Boolean,
    val message: String? = null,
    //val activeColor: Color? = null
    val activeState: SlotActorStates = SlotActorStates.NO_ACTION
)
