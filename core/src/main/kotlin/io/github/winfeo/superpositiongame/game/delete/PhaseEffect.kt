//package io.github.winfeo.superpositiongame.game.effect.effect
//
//import io.github.winfeo.superpositiongame.model.game.GameState
//import io.github.winfeo.superpositiongame.game.effect.CardEffect
//import io.github.winfeo.superpositiongame.model.card.Card
//import io.github.winfeo.superpositiongame.model.dice.DiceState
//
//class PhaseEffect: CardEffect {
//    override fun apply(
//        state: GameState,
//        card: Card,
//        targetSlotIndex: Int,
//        playerId: String
//    ): GameState {
//        val player = state.players[playerId]?: return state
//        val slots = player.slots.toMutableList()
//        val slot = slots[targetSlotIndex]
//
//        val isForward = card.isForwardRotation?: return state
//        val newDice = when (slot.dice.state) {
//            DiceState.PLUS -> if (isForward) DiceState.I else DiceState.I_MINUS
//            DiceState.MINUS -> if (isForward) DiceState.I_MINUS else DiceState.I
//            DiceState.I -> if (isForward) DiceState.MINUS else DiceState.PLUS
//            DiceState.I_MINUS -> if (isForward) DiceState.PLUS else DiceState.MINUS
//            else -> slot.dice.state
//        }
//
//        slots[targetSlotIndex] = slot.copy(
//            dice = slot.dice.copy(state = newDice),
//            appliedCards = slot.appliedCards + card
//        )
//
//        val updatedPlayer = player.copy(slots = slots)
//        return state.copy(
//            players = state.players + (playerId to updatedPlayer)
//        )
//    }
//}
