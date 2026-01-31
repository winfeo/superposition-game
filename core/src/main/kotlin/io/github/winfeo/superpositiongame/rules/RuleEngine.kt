package io.github.winfeo.superpositiongame.rules

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.SlotActorStates
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.components.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.rules.model.ValidationResult
import io.github.winfeo.superpositiongame.rules.model.RuleContext
import io.github.winfeo.superpositiongame.ui.CardAndDiceContainer
import ktx.collections.lastIndex

//Проверка игровых правил
object RuleEngine {

    fun checkRules(ruleContext: RuleContext): ValidationResult {
        val card = ruleContext.card
        val cardSlot = ruleContext.targetSlot
        val diceSlot = cardSlot.getDiceActor()

        if (cardSlot.isFrozenSlot()) {
            return ValidationResult(
                canPlace = false,
                message = "Невозможно использовать карту для кубита (кубит заморожен)",
                activeState = SlotActorStates.HOVERED_CANT_PLACE
            )
        }

        if (!isCardCompatibleWithArrow(card, diceSlot.dice.state)) {
            return ValidationResult(
                canPlace = false,
                message = "Невозможно использовать эту карту для кубита (отсутствует нужный цвет)",
                activeState = SlotActorStates.HOVERED_CANT_PLACE
            )
        }

        if (card.type in listOf(CardType.PAULI_X3, CardType.PAULI_Y3, CardType.PAULI_Z3, CardType.HADAMARD_H3)) {
            if(!isBorderSlot(cardSlot)) {
                return ValidationResult(
                    canPlace = false,
                    message = "Невозможно использовать эту карту для кубита (кубит является крайним)",
                    activeState = SlotActorStates.HOVERED_CANT_PLACE
                )
            }
        }

        return ValidationResult(
            canPlace = true,
            message = null,
            activeState = SlotActorStates.HOVERED_CAN_PLACE
        )
    }

    //Проверка на наличие стрелок на кубите соответ игровой карте
    private fun isCardCompatibleWithArrow(card: Card, diceState: DiceState): Boolean {
        ///TODO проверка на соотвествие цветов стролок на карте
        ///TODO добавить в модель карты цвет и в состояние кубика список цветов и искать цвет среди списка?
        val axis = when (card.type) {
            CardType.PAULI_X, CardType.PAULI_X3, CardType.ROTATE_X -> AxisRotation.X
            CardType.PAULI_Y, CardType.PAULI_Y3,CardType.ROTATE_Y -> AxisRotation.Y
            CardType.PAULI_Z, CardType.PAULI_Z3,CardType.ROTATE_Z, CardType.PHASE_S, CardType.PHASE_S_BACKWARDS -> AxisRotation.Z
            CardType.HADAMARD_H, CardType.HADAMARD_H3 -> return true
            else -> return true //TODO спец карты пока не проверяем, добавить
        }

        return hasArrowForAxis(diceState, axis)
    }

    private fun hasArrowForAxis(state: DiceState, axis: AxisRotation): Boolean {
        return when (axis) {
            AxisRotation.X -> when (state) {
                DiceState.ZERO, DiceState.ONE, DiceState.I, DiceState.I_MINUS -> true
                else -> false
            }
            AxisRotation.Y -> when (state) {
                DiceState.ZERO, DiceState.ONE, DiceState.PLUS, DiceState.MINUS -> true
                else -> false
            }
            AxisRotation.Z -> when (state) {
                DiceState.PLUS, DiceState.MINUS, DiceState.I, DiceState.I_MINUS -> true
                else -> false
            }
        }
    }

    //Проверка, что слот не является крайним (для применения x-3 гейтов)
    private fun isBorderSlot(cardSlot: SlotActor): Boolean {
        val container = cardSlot.parent as CardAndDiceContainer
        val parentTableChildren = (container.parent as Table).children
        val index = parentTableChildren.indexOf(container)
        return (index != 0 && index != parentTableChildren.lastIndex)
    }
}
