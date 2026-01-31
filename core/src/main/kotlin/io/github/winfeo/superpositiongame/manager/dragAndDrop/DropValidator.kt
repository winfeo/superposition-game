package io.github.winfeo.superpositiongame.manager.dragAndDrop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.dice.DiceActor
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.DiceChangerManager
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.components.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.rules.RuleEngine
import io.github.winfeo.superpositiongame.rules.model.RuleContext
import io.github.winfeo.superpositiongame.rules.model.ValidationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Класс устанавливает кубиты в новое состояние
class DropValidator(
    private val stage: Stage = GameConfig.stage,
    private val scope: CoroutineScope
) {

    fun canAccept(
        payload: CardDragPayload,
        target: Actor
    ): ValidationResult {
        val ruleContext = toRuleContext(payload, target)
        val validationResult: ValidationResult = RuleEngine.checkRules(ruleContext)
        return validationResult
    }
    ///TODO вынести установку и валидацию по разным классам?

    fun onDrop(
        payload: CardDragPayload,
        target: Actor
    ) {
        val cardActor = payload.sourceActor as CardActor
        val slot = target as SlotActor
        val dice = slot.getDice()
        val cardType = cardActor.card.type

        cardActor.remove()
        scope.launch {
            val newState: Map<DiceActor, DiceState> = defineNewState(cardType, dice)
            Gdx.app.postRunnable {
                ///TODO сделать чтобы карта клалась в слот до вызова диалогового окна, а не после отрисовывалась
                slot.placeCard(cardActor)
                newState.forEach { (diceSlot, diceState) ->
                    diceSlot.changeState(diceState)
                }
            }
        }
    }

    private fun toRuleContext(
        payload: CardDragPayload,
        target: Actor
    ): RuleContext {
        return RuleContext(
            card = (payload.sourceActor as CardActor).card, //модель игровой карты
            targetSlot = target as SlotActor, //слот куда хотят положить карту
        )
    }

    private suspend fun defineNewState(
        card: CardType,
        dice: DiceActor
    ): Map<DiceActor, DiceState> {
        return when (card) {
            ///TODO сдеать всё через корутины?
            CardType.PAULI_X -> mapOf(dice to DiceChangerManager.pauliGateX(dice.dice.state))
            CardType.PAULI_Y -> mapOf(dice to DiceChangerManager.pauliGateY(dice.dice.state))
            CardType.PAULI_Z -> mapOf(dice to DiceChangerManager.pauliGateZ(dice.dice.state))
            CardType.PHASE_S -> mapOf(dice to DiceChangerManager.phaseGate(dice.dice.state, true))
            CardType.PHASE_S_BACKWARDS -> mapOf(dice to DiceChangerManager.phaseGate(dice.dice.state, false))
            CardType.HADAMARD_H -> mapOf(dice to DiceChangerManager.hadamardGate(dice.dice.state))
            CardType.ROTATE_X -> mapOf(dice to DiceChangerManager.rotateGate(stage, AxisRotation.X, dice.dice.state))
            CardType.ROTATE_Y -> mapOf(dice to DiceChangerManager.rotateGate(stage, AxisRotation.Y, dice.dice.state))
            CardType.ROTATE_Z -> mapOf(dice to DiceChangerManager.rotateGate(stage, AxisRotation.Z, dice.dice.state))
            CardType.PAULI_X3, CardType.PAULI_Y3, CardType.PAULI_Z3, CardType.HADAMARD_H3
                 -> DiceChangerManager.tripleEffectGates(dice, card)
            ///TODO Никогда не войдёт сюда?
            else -> mapOf(dice to dice.dice.state)
        }
    }
}
