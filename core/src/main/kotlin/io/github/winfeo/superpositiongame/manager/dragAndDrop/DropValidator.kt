package io.github.winfeo.superpositiongame.manager.dragAndDrop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.SlotActorStates
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.dice.DiceActor
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.game.GameCycle
import io.github.winfeo.superpositiongame.game.controller.TurnContext
import io.github.winfeo.superpositiongame.manager.DiceChangerManager
import io.github.winfeo.superpositiongame.manager.PlayerHandManager
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.components.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.rules.RuleEngine
import io.github.winfeo.superpositiongame.rules.model.RuleContext
import io.github.winfeo.superpositiongame.rules.model.ValidationResult
import io.github.winfeo.superpositiongame.ui.CardAndDiceContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Класс устанавливает кубиты в новое состояние (карты, которые играются на игровое поле)
class DropValidator(
    private val stage: Stage = GameConfig.stage,
    private val scope: CoroutineScope,
    private val playerHand: PlayerHandManager
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
        val sourceCardActor = payload.sourceActor as CardActor
        val targetSlot = target as SlotActor
        //val dice = targetSlot.getDiceActor()
        //val cardType = sourceCardActor.card.type

        println("---- DROP CARD ----")
        println("Previous: ${targetSlot.previousCardActor} type: ${targetSlot.previousCardActor?.card?.type}, hash=${System.identityHashCode(targetSlot.previousCardActor?.card)}")
        println("New: ${sourceCardActor.card.type} hash=${System.identityHashCode(sourceCardActor)}")

//        val previousCard = (sourceCardActor.parent as SlotActor).children.firstOrNull {it is CardActor} as? CardActor
//        val previousCard = targetSlot.children.firstOrNull {it is CardActor} as? CardActor
        //targetSlot.placeCard(sourceCardActor)

        //sourceCardActor.remove()
        playerHand.removeCard(sourceCardActor)
        scope.launch {
            val newState: Map<DiceActor, DiceState>? = defineNewState(sourceCardActor, targetSlot)
            Gdx.app.postRunnable {
                ///TODO сделать чтобы карта клалась в слот до вызова диалогового окна, а не после отрисовывалась
                sourceCardActor.canDrag = false
                sourceCardActor.touchable = Touchable.disabled
                ///TODO не обновляется слушатель, поэтому всё-равно ссылается слот на EMPTY карту?
                //sourceCardActor.setPreviousMoveCard()
                //targetSlot.getDiceActor().setPreviousMoveDice()

                ///TODO размеры карты при раздаче увеличиваю, а тут уменьшаю, переделать
                sourceCardActor.setSize(sourceCardActor.width / 1.5f, sourceCardActor.height / 1.5f)
                sourceCardActor.rotation = 0f
                if (sourceCardActor.card.type != CardType.QUANTUM_NOISE) {
                    targetSlot.placeCard(sourceCardActor) ///TODO не помещать карту отмены в слот!
                }
                newState?.forEach { (diceSlot, diceState) ->
                    diceSlot.changeState(diceState)
                    DiceChangerManager.changeDiceStateColor(diceSlot)
                }

                GameCycle.playerMoveController.moveMade()

                if (TurnContext.isMultiplicationActive && ///TODO переделать!
                    TurnContext.lockedArea == null) {

                    TurnContext.lockedArea = targetSlot.getSlotActorArea()
                }
            }
        }
    }

    private fun toRuleContext(
        payload: CardDragPayload,
        target: Actor
    ): RuleContext {
        return RuleContext(
            card = (payload.sourceActor as CardActor)/*.card*/, //модель игровой карты
            targetSlot = target as SlotActor, //слот куда хотят положить карту
        )
    }

    private suspend fun defineNewState(
        cardActor: CardActor,
        targetSlot: SlotActor
    ): Map<DiceActor, DiceState>? {
        val dice: DiceActor = targetSlot.getDiceActor()
        val card: CardType = cardActor.card.type
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
                 -> DiceChangerManager.tripleEffectGates(currentDice = dice, card = card)
            ///TODO разделить логику?
            CardType.MEASUREMENT -> {
                DiceChangerManager.measurementCardEffect(targetSlot)
                null
            }
            CardType.QUANTUM_NOISE -> {
                DiceChangerManager.quantumNoiseEffect(dice = dice, targetSlot = targetSlot)
                null
            }
            ///TODO Никогда не войдёт сюда?
            else -> mapOf(dice to dice.dice.state)
        }
    }
}
