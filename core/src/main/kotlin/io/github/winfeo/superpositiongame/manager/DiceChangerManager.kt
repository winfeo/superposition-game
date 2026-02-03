package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.dice.DiceActor
import io.github.winfeo.superpositiongame.model.card.components.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.graphics.RotateSelectionDialog
import io.github.winfeo.superpositiongame.model.card.CardType

//Отвечает за преобразования кубитов из одного состояния в другое
object DiceChangerManager {

    //Pauli гейты (180 градусов)
    fun pauliGateX(currentState: DiceState): DiceState {
        return when (currentState) {
            DiceState.ZERO -> DiceState.ONE
            DiceState.ONE -> DiceState.ZERO
            DiceState.I -> DiceState.I_MINUS
            DiceState.I_MINUS -> DiceState.I
            else -> currentState
            ///TODO можно ли повернуть, если нет стрелки соотвествующего цвета на этих гранях?
//            DiceState.PLUS -> DiceState.MINUS
//            DiceState.MINUS -> DiceState.PLUS
        }
    }

    fun pauliGateY(currentState: DiceState): DiceState {
        return when (currentState) {
            DiceState.PLUS -> DiceState.MINUS
            DiceState.MINUS -> DiceState.PLUS
            DiceState.ZERO -> DiceState.ONE
            DiceState.ONE -> DiceState.ZERO
            else -> currentState
            ///TODO можно ли повернуть, если нет стрелки соотвествующего цвета на этих гранях?
//            DiceState.I -> DiceState.I
//            DiceState.I_MINUS -> DiceState.I_MINUS
        }
    }

    fun pauliGateZ(currentState: DiceState): DiceState {
        return when (currentState) {
            DiceState.PLUS -> DiceState.MINUS
            DiceState.MINUS -> DiceState.PLUS
            DiceState.I -> DiceState.I_MINUS
            DiceState.I_MINUS -> DiceState.I
            else -> currentState
            ///TODO можно ли повернуть, если нет стрелки соотвествующего цвета на этих гранях?
//            DiceState.ZERO -> DiceState.ZERO
//            DiceState.ONE -> DiceState.ONE
        }
    }

    //Phase гейты (90 градусов вокруг Z)
    fun phaseGate(currentState: DiceState, isForward: Boolean): DiceState {
        return when (currentState) {
            DiceState.PLUS -> if (isForward) DiceState.I else DiceState.I_MINUS
            DiceState.MINUS -> if (isForward) DiceState.I_MINUS else DiceState.I
            DiceState.I -> if (isForward) DiceState.MINUS else DiceState.PLUS
            DiceState.I_MINUS -> if (isForward) DiceState.PLUS else DiceState.MINUS
            else -> currentState
            ///TODO можно ли повернуть, если нет стрелки соотвествующего цвета на этих гранях?
//            DiceState.ZERO -> DiceState.ZERO
//            DiceState.ONE -> DiceState.ONE
        }
    }

    //Hadamard гейты (90 вдоль Y, иначе 180)
    fun hadamardGate(currentState: DiceState): DiceState {
        return when (currentState) {
            DiceState.ZERO -> DiceState.PLUS
            DiceState.ONE -> DiceState.MINUS
            DiceState.PLUS -> DiceState.ZERO
            DiceState.MINUS -> DiceState.ONE
            DiceState.I -> DiceState.I_MINUS
            DiceState.I_MINUS -> DiceState.I
        }
    }

    //Rotate гейты (любой угол вдоль осей)
    suspend fun rotateGate(
        stage: Stage,
        axisRotation: AxisRotation,
        currentState: DiceState
    ): DiceState {
        val availableStates = when (axisRotation) {
            AxisRotation.X -> listOf(DiceState.ONE, DiceState.ZERO, DiceState.I, DiceState.I_MINUS)
            AxisRotation.Y -> listOf(DiceState.ONE, DiceState.ZERO, DiceState.PLUS, DiceState.MINUS)
            AxisRotation.Z -> listOf(DiceState.PLUS, DiceState.MINUS, DiceState.I, DiceState.I_MINUS)
        }.filter { it != currentState }

        val userChoice = RotateSelectionDialog.show(stage, availableStates)
        return userChoice
    }

    //x-3 эффекты гейтов
    fun tripleEffectGates(currentDice: DiceActor, card: CardType): Map<DiceActor, DiceState> {
        val dicesActorsToChange: Map<SlotActor, DiceActor>/*List<DiceActor>*/ = TripleEffectManager.findDices(currentDice)
        val newDicesStates = mutableMapOf<DiceActor, DiceState>()
        dicesActorsToChange.forEach { (slotActor, diceActor) ->
            if (!slotActor.isFrozenSlot()) {
                when (card) {
                    CardType.PAULI_X3 -> { ///TODO если под заморозкой, то нельзя поменять?
                        val newState = pauliGateX(diceActor.dice.state)
                        newDicesStates.put(diceActor, newState)
                    }
                    CardType.PAULI_Y3 -> {
                        val newState = pauliGateY(diceActor.dice.state)
                        newDicesStates.put(diceActor, newState)
                    }
                    CardType.PAULI_Z3 -> {
                        val newState = pauliGateZ(diceActor.dice.state)
                        newDicesStates.put(diceActor, newState)
                    }
                    else -> {
                        val newState = hadamardGate(diceActor.dice.state)
                        newDicesStates.put(diceActor, newState)
                    }
                }
            }
        }
        return newDicesStates
    }

    //Заморозка
    fun measurementCardEffect(slot: SlotActor) {
        slot.changeFreezeState()
    }

    //Отмена последнего действия
    fun quantumNoiseEffect(dice: DiceActor, targetSlot: SlotActor) {
        if (targetSlot.isFrozenSlot()) {
            targetSlot.changeFreezeState()
        } else {
            dice.setPreviousMoveDiceType() ///TODO вынести обработку дайсов в отдельный слот
        }
        targetSlot.undoCard()
        ///TODO хранить список состояний, чтобы можно было на любой стейт откатиться?
    }

}
