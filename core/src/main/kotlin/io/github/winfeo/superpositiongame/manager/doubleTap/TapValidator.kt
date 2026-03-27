//package io.github.winfeo.superpositiongame.manager.doubleTap
//
//import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
//import io.github.winfeo.superpositiongame.model.card.CardType
//import kotlinx.coroutines.CoroutineScope
//
////Класс для применения соотвествующих эффектов карт (карты, которые играются в сброс)
//class TapValidator(
//    private val scope: CoroutineScope,
//    private val playerHand: PlayerHandManager,
//    moveController: PlayerMoveController
//) {
//    private val diceSwapManager = DiceSwapManager()
//    private val multiplicationEffect = MultiplicationEffectManager(moveController)
//    private val reshaffleCardEffect = ReshaffleCardEffect()
//
//    fun onAccept(card: CardActor) {
//        val cardType: CardType = card.card.type
////        scope.launch {
////            defineCardEffect(cardType)
////        }
//        defineCardEffect(cardType)
//        playerHand.removeCard(card)
//        //card.remove()
//
//    }
//
//    private fun defineCardEffect(card: CardType) {
//        when (card) {
//            CardType.SWAP -> diceSwapManager.applyEffect()
//            CardType.KRONECKER_MULTIPLICATION -> multiplicationEffect.applyEffect()
////            CardType.RESHAFFLE -> reshaffleCardEffect.applyEffect()
//            else -> GameCycle.playerMoveController.moveMade()
//        }
//    }
//}
