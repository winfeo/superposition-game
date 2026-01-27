package io.github.winfeo.superpositiongame.rules

import io.github.winfeo.superpositiongame.actor.SlotActorStates
import io.github.winfeo.superpositiongame.rules.model.ValidationResult
import io.github.winfeo.superpositiongame.rules.model.RuleContext

object RuleEngine {

    fun checkRules(ruleContext: RuleContext): ValidationResult {
        val canPlace = ruleContext.targetSlot.getCard().card.id == "empty"
        return ValidationResult(
            canPlace = canPlace,
            message = "Слот уже занят",
            activeState = if (canPlace) SlotActorStates.HOVERED_CAN_PLACE else SlotActorStates.HOVERED_CANT_PLACE
        )
    }
}
