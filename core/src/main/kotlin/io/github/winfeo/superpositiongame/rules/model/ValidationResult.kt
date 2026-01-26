package io.github.winfeo.superpositiongame.rules.model

import io.github.winfeo.superpositiongame.actor.SlotActorStates

data class ValidationResult(
    val canPlace: Boolean,
    val message: String? = null,
    //val activeColor: Color? = null
    val activeState: SlotActorStates = SlotActorStates.NO_ACTION
)
