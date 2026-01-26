package io.github.winfeo.superpositiongame.rules

import io.github.winfeo.superpositiongame.actor.SlotActorStates
import io.github.winfeo.superpositiongame.rules.model.ValidationResult
import io.github.winfeo.superpositiongame.rules.model.RuleContext

object RuleEngine {

    fun checkRules(ruleContext: RuleContext): ValidationResult {
        return ValidationResult(
            canPlace = true,
            message = "Отладка. Тест",
            activeState = SlotActorStates.HOVERED_CAN_PLACE
        )
    }
}
