package io.github.winfeo.superpositiongame.game.controller

import com.badlogic.gdx.scenes.scene2d.Touchable
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.actor.card.CardFactory
import io.github.winfeo.superpositiongame.actor.dice.DiceActor
import io.github.winfeo.superpositiongame.manager.DiceChangerManager
import io.github.winfeo.superpositiongame.manager.dragAndDrop.DropValidator
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.rules.RuleEngine
import io.github.winfeo.superpositiongame.rules.model.RuleContext
import io.github.winfeo.superpositiongame.ui.CardAndDiceContainer
import io.github.winfeo.superpositiongame.ui.GameTable
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.random.Random

class OpponentMoveController(
    private val table: GameTable,
    private val dropValidator: DropValidator
) {
    private var continuation: CancellableContinuation<Unit>? = null
    suspend fun makeMove() {

        val opponentSlotsContainer = getOpponentSlots()
        val playerSlotsContainer = getPlayerSlots()

        val card = CardFactory.createDraggableCardForOpponentScript()
        val cardActor = CardActorBuilder.createCardActorFromModel(card)

        delay(Random.nextLong(3000, 7000))

        var isPlaced = false
        if (Random.nextFloat() < 0.15f) {
            isPlaced = tryPlaceCard(cardActor, playerSlotsContainer)
        }
        if (!isPlaced) {
            tryPlaceCard(cardActor, opponentSlotsContainer)
        }
    }

    private suspend fun tryPlaceCard(
        card: CardActor,
        containers: List<CardAndDiceContainer>
    ): Boolean {
        for (container in containers.shuffled()) {
            val cardSlot: SlotActor = container.cardSlot
            val diceSlot: SlotActor = container.diceSlot

            val context = RuleContext(
                card = card,
                targetSlot = cardSlot
            )
            val result = RuleEngine.checkRules(context)
            if (result.canPlace) {
                val diceActor = diceSlot.getDiceActor()
                val newState: Map<DiceActor, DiceState>? = when (card.card.type) {
                    CardType.ROTATE_X -> {
                        val state = listOf(DiceState.ONE, DiceState.ZERO, DiceState.I, DiceState.I_MINUS).filter { it != diceActor.dice.state }.random()
                        mapOf(diceActor to state)
                    }
                    CardType.ROTATE_Y -> {
                        val state = listOf(DiceState.ONE, DiceState.ZERO, DiceState.PLUS, DiceState.MINUS).filter { it != diceActor.dice.state }.random()
                        mapOf(diceActor to state)
                    }
                    CardType.ROTATE_Z -> {
                        val state = listOf(DiceState.PLUS, DiceState.MINUS, DiceState.I, DiceState.I_MINUS).filter { it != diceActor.dice.state }.random()
                        mapOf(diceActor to state)
                    }
                    CardType.MEASUREMENT -> {
                        DiceChangerManager.measurementCardEffect(cardSlot)
                        null
                    }
                    CardType.QUANTUM_NOISE -> {
                        DiceChangerManager.quantumNoiseEffect(dice = diceSlot.getDiceActor(), targetSlot = cardSlot)
                        null
                    }
                    else -> dropValidator.defineNewState(card, cardSlot)
                }

                card.canDrag = false
                card.touchable = Touchable.disabled
                newState?.forEach { (diceSlot, diceState) ->
                    diceSlot.changeState(diceState)
                    DiceChangerManager.changeDiceStateColor(diceSlot)
                    cardSlot.placeCard(card)
                }
                return true
            }
        }
        return false
    }

    fun finishMove() {
        continuation?.resume(Unit)
        continuation = null
    }

    private fun getOpponentSlots(): List<CardAndDiceContainer> {
        return table.getOpponentSlots()
    }
    private fun getPlayerSlots(): List<CardAndDiceContainer> {
        return table.getPlayerSlots()
    }
}
