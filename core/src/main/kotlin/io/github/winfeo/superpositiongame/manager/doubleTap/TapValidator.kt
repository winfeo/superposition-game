package io.github.winfeo.superpositiongame.manager.doubleTap

import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.manager.DiceSwapManager
import io.github.winfeo.superpositiongame.model.card.CardType
import kotlinx.coroutines.CoroutineScope

//Класс для применения соотвествующих эффектов карт (карты, которые играются в сброс)
class TapValidator(
    private val scope: CoroutineScope
) {
    private val diceSwapManager = DiceSwapManager()

    fun onAccept(card: CardActor) {
        val cardType: CardType = card.card.type
//        scope.launch {
//            defineCardEffect(cardType)
//        }
        defineCardEffect(cardType)
        card.remove()

    }

    private fun defineCardEffect(card: CardType) {
        when (card) {
            CardType.SWAP -> diceSwapManager.applyEffect()
            else -> return
        }
    }
}
