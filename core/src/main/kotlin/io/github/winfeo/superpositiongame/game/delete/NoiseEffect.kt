//package io.github.winfeo.superpositiongame.game.effect.effect
//
//import io.github.winfeo.superpositiongame.model.game.GameState
//import io.github.winfeo.superpositiongame.game.effect.CardEffect
//import io.github.winfeo.superpositiongame.model.card.Card
//import io.github.winfeo.superpositiongame.model.dice.Dice
//import io.github.winfeo.superpositiongame.model.dice.DiceState
//
/////TODO разобраться ещё раз как работает, отатывает ли состояние?
//class NoiseEffect: CardEffect {
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
//        if (slot.appliedCards.isEmpty()) return state
//        val previousCards = slot.appliedCards.dropLast(1)
//        val previousDiceState = calculatePreviousDiceState(
//            initialDice = slot.initialDice,
//            appliedCards = previousCards
//        )
//
//        val updatedSlot = slot.copy(
//            appliedCards = previousCards,
//            dice = previousDiceState
//        )
//        slots[targetSlotIndex] = updatedSlot
//        val updatedPlayer = player.copy(slots = slots)
//
//        return state.copy(
//            players = state.players + (playerId to updatedPlayer)
//        )
//    }
//
//    private fun calculatePreviousDiceState(
//        initialDice: Dice,
//        appliedCards: List<Card>
//    ): Dice {
////        appliedCards.forEach { card ->
////
////        }
//        return Dice(id = "33343", state = DiceState.PLUS, requiredState = DiceState.I)
//    }
//}
