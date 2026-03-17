package io.github.winfeo.superpositiongame.game

import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.graphics.RotateSelectionDialog
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GameMoveType
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.rule.RuleEngine
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult
import io.github.winfeo.superpositiongame.ui.actor.SlotActor
import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//Проверят можно ли разместить карту в слоте и отправляет Move на сервер
class PlayerActionController(
    private val playerId: String,
    private val getOpponentId: () -> String,
    private val stage: Stage,
    private val scope: CoroutineScope,
    private val onMove: (Move) -> Unit,
    private val getGameState: () -> GameState
) {

    fun canDrop(
        cardActor: CardActor,
        slotActor: SlotActor
    ): ValidationResult {
        val gameState = getGameState()
        val player = gameState.players[playerId]
        val opponent = gameState.players.values.first { it.id != playerId }

        ///TODO в утилиту?
        val targetSlot =
            if (slotActor.slotOwner == SlotOwner.PLAYER) player!!.slots[slotActor.slotIndex]
            else opponent.slots[slotActor.slotIndex]

        val context = RuleContext(
            card = cardActor.card,
            targetSlot = targetSlot,
            playerSlots = player!!.slots,
            opponentSlots = opponent.slots,
            activeSlotsRow = gameState.activeSlotsRow,
        )

        return RuleEngine.checkRules(context)
    }

    fun dropCard(
        cardActor: CardActor,
        slotActor: SlotActor
    ) {
        val state = getGameState()

        if (state.currentPlayerId != playerId) return
        scope.launch {
            val move = createMove(cardActor, slotActor)
            onMove(move)
        }
    }

    private suspend fun createMove(
        cardActor: CardActor,
        slotActor: SlotActor
    ): Move {
        //TODO дописать. в зависимости от карты - команду отправлять соотвествующую
        return when (cardActor.card.type) {
            ///TODO может быть вынеси в другое место, чтобы не тянуть scope и stage
            CardType.ROTATE -> {
                val availableStates = getRotateDiceStates(
                    cardAxis = cardActor.card.axis!!,
                    diceState = slotActor.getDiceActor().dice.state
                )

                val dialog = RotateSelectionDialog()
                val selectedState = dialog.show(
                    stage = stage,
                    availableStates = availableStates
                )

                val targetPlayerId = if (slotActor.slotOwner == SlotOwner.PLAYER) playerId else getOpponentId()
                Move.RotateDice(
                    playerId = playerId,
                    cardId = cardActor.card.id,
                    targetSlotIndex = slotActor.slotIndex,
                    newState = selectedState,
                    targetPlayerId = targetPlayerId
                )
            }
//            CardType.SWAP -> {
//
//
//                Move.SwapDices(
//                    playerId = playerId,
//                    firstSlotIndex = TODO(),
//                    secondSlotIndex = TODO()
//                )
//            }
            else -> {
                val targetPlayerId = if (slotActor.slotOwner == SlotOwner.PLAYER) playerId else getOpponentId()
                Move.PlayCard(
                    playerId = playerId,
                    type = GameMoveType.PLAY_CARD,
                    cardId = cardActor.card.id,
                    targetSlotIndex = slotActor.slotIndex,
                    targetPlayerId = targetPlayerId
                )
            }
        }
    }

    ///TODO в утилиту вынести?
    private fun getRotateDiceStates(
        cardAxis: AxisRotation,
        diceState: DiceState
    ): List<DiceState> {
        return when (cardAxis) {
            AxisRotation.X -> {
                listOf(DiceState.ZERO, DiceState.I, DiceState.ONE, DiceState.I_MINUS).filter { it != diceState }
            }
            AxisRotation.Y -> {
                listOf(DiceState.ZERO, DiceState.PLUS, DiceState.ONE, DiceState.MINUS).filter { it != diceState }
            }
            AxisRotation.Z -> {
                listOf(DiceState.PLUS, DiceState.I, DiceState.MINUS, DiceState.I_MINUS).filter { it != diceState }
            }
        }
    }


}
