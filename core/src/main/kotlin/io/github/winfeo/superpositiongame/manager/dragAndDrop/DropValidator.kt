package io.github.winfeo.superpositiongame.manager.dragAndDrop

import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.rules.RuleEngine
import io.github.winfeo.superpositiongame.rules.model.RuleContext
import io.github.winfeo.superpositiongame.rules.model.ValidationResult

// Класс для получения ответа от класса проверки правил, можно ли установить карту в слот
class DropValidator() {

    fun canAccept(
        payload: CardDragPayload,
        target: Actor
    ): ValidationResult {
        val ruleContext = toRuleContext(payload, target)
        val validationResult: ValidationResult = RuleEngine.checkRules(ruleContext)
        return validationResult
    }

    fun onDrop(
        payload: CardDragPayload,
        target: Actor
    ) {
        val card = payload.sourceActor as CardActor
        val slot = target as SlotActor

        card.remove()
        slot.placeCard(card)
    }

    private fun toRuleContext(
        payload: CardDragPayload,
        target: Actor
    ): RuleContext {
        return RuleContext(
            card = (payload.sourceActor as CardActor).card, //модель игровой карты
            targetSlot = target as SlotActor //слот куда хотят положить карту
        )
    }
}
