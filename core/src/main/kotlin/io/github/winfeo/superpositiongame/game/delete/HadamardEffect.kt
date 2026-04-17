//package io.github.winfeo.superpositiongame.game.effect.effect
//
//import io.github.winfeo.superpositiongame.model.game.GameState
//import io.github.winfeo.superpositiongame.game.effect.CardEffect
//import io.github.winfeo.superpositiongame.model.card.Card
//import io.github.winfeo.superpositiongame.model.dice.DiceState
//import io.github.winfeo.superpositiongame.rule.rule.ArrowCompatibilityRule
//
//class HadamardEffect: CardEffect {
//    override fun apply(
//        state: GameState,
//        card: Card,
//        targetSlotIndex: Int,
//        playerId: String
//    ): GameState {
//        val player = state.players[playerId]?: return state
//        val slots = player.slots.toMutableList()
//
//        val arrowRule = ArrowCompatibilityRule()
//
//        val indexes =
//            if (card.actionRadius == 3) listOf(targetSlotIndex - 1, targetSlotIndex, targetSlotIndex + 1)
//            else listOf(targetSlotIndex)
//
//        indexes.forEach { index ->
//            val slot = slots[index]
//
//            if (index != targetSlotIndex) {
//                if (!arrowRule.isCardCompatibleWithArrow(card, slot.dice)) {
//                    return@forEach
//                }
//            }
//
//            val newDice = when (slot.dice.state) {
//                DiceState.ZERO -> DiceState.PLUS
//                DiceState.ONE -> DiceState.MINUS
//                DiceState.PLUS -> DiceState.ZERO
//                DiceState.MINUS -> DiceState.ONE
//                DiceState.I -> DiceState.I_MINUS
//                DiceState.I_MINUS -> DiceState.I
//            }
//
//            slots[index] = slot.copy(
//                dice = slot.dice.copy(state = newDice),
//                appliedCards =
//                    if (index == targetSlotIndex) slot.appliedCards + card
//                    else slot.appliedCards
//            )
//        }
//
//        val updatedPlayer = player.copy(slots = slots)
//        return state.copy(
//            players = state.players + (playerId to updatedPlayer)
//        )
//    }
//}
