package io.github.winfeo.superpositiongame.manager.doubleTap

import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.game.GameCycle
import io.github.winfeo.superpositiongame.game.controller.PlayerMoveController
import io.github.winfeo.superpositiongame.game.controller.TurnContext
import io.github.winfeo.superpositiongame.manager.DiceSwapManager
import io.github.winfeo.superpositiongame.manager.MultiplicationEffectManager
import io.github.winfeo.superpositiongame.manager.PlayerHandManager
import io.github.winfeo.superpositiongame.manager.ReshaffleCardEffect
import io.github.winfeo.superpositiongame.model.card.CardType
import kotlinx.coroutines.CoroutineScope

//Класс для применения соотвествующих эффектов карт (карты, которые играются в сброс)
class TapValidator(
    private val scope: CoroutineScope,
    private val playerHand: PlayerHandManager,
    moveController: PlayerMoveController
) {
    private val diceSwapManager = DiceSwapManager()
    private val multiplicationEffect = MultiplicationEffectManager(moveController)
    private val reshaffleCardEffect = ReshaffleCardEffect()

    fun onAccept(card: CardActor) {
        val cardType: CardType = card.card.type
//        scope.launch {
//            defineCardEffect(cardType)
//        }
        defineCardEffect(cardType)
        playerHand.removeCard(card)
        GameCycle.playerMoveController.moveMade()
        //card.remove()

    }

    private fun defineCardEffect(card: CardType) {
        when (card) {
            CardType.SWAP -> diceSwapManager.applyEffect()
            CardType.KRONECKER_MULTIPLICATION -> multiplicationEffect.applyEffect()
//            CardType.RESHAFFLE -> reshaffleCardEffect.applyEffect()
            else -> return
        }
    }
}
