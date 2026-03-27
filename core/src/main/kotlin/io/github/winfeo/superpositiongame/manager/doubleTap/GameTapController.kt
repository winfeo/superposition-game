//package io.github.winfeo.superpositiongame.manager.doubleTap
//
//import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//
////Контроллер (слушатель) использования специальных карт (двойное нажатие на них)
//class GameTapController() {
//    private lateinit var playerHand: PlayerHandManager
//
//    fun init(playerHand: PlayerHandManager) {
//        this.playerHand = playerHand
//    }
//    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//    ///TODO подумать над тем, чтобы не передавать скоуп как параметр
//    private val touchManager = CardsTapManager(this, scope)
//    private lateinit var tapValidator: TapValidator
//    fun setupCard(card: CardActor) {
//        touchManager.makeCardTouchable(card)
//        tapValidator = TapValidator(scope = scope, playerHand, playerMoveController)
//    }
//
//
//    fun onCardTapped(card: CardActor) {
//        if (!GameCycle.gameManager.isPlayerMove()) return
//        println("Отладка. Нажали на специальную карту: ${card.card.id}")
//    }
//
//    fun onCardDoubleTapped(card: CardActor) {
//        println("Отладка. Активируем специальную карту: ${card.card.id}")
//        tapValidator.onAccept(card)
//    }
//
//    fun dispose() {
//        touchManager.clear()
//        scope.cancel()
//    }
//
//}
