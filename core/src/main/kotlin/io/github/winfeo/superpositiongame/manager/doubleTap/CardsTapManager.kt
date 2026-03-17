//package io.github.winfeo.superpositiongame.manager.doubleTap
//
//import com.badlogic.gdx.scenes.scene2d.InputEvent
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
//import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
//import io.github.winfeo.superpositiongame.config.GameConfig
//import kotlinx.coroutines.CoroutineScope
//import ktx.actors.alpha
//
////Логика обработки специальных карт
//class CardsTapManager(
//    currentListener: GameTapController,
//    scope: CoroutineScope
//) {
//    private val listener: GameTapController = currentListener
//    private val clickListeners = mutableMapOf<CardActor, ClickListener>()
//    private val lastTapTimes = mutableMapOf<CardActor, Long>()
//    private val doubleTapInterval = GameConfig.getDoubleTapIntervalTime()
//
//    fun makeCardTouchable(card: CardActor) {
//        if (clickListeners.containsKey(card)) return
//
//        val clickListener = object : ClickListener() {
//            override fun clicked(event: InputEvent?, x: Float, y: Float) {
//                handleCardClick(card)
//            }
//        }
//
//        card.addListener(clickListener)
//        clickListeners[card] = clickListener
//    }
//
//    private fun handleCardClick(card: CardActor) {
//        val currentTime = System.currentTimeMillis()
//        val lastTime = lastTapTimes[card]
//
//        if (lastTime == null || currentTime - lastTime > doubleTapInterval) {
//            lastTapTimes[card] = currentTime
//            listener.onCardTapped(card)
//        } else {
//            lastTapTimes.remove(card)
//            removeCardTouchable(card)
//            listener.onCardDoubleTapped(card)
//            card.alpha = 0.3f
//        }
//    }
//
//    fun removeCardTouchable(card: CardActor) {
//        clickListeners[card]?.let { listener ->
//            card.removeListener(listener)
//        }
//        clickListeners.remove(card)
//        lastTapTimes.remove(card)
//    }
//
//    fun clear() {
//        clickListeners.forEach { (card, listener) ->
//            card.removeListener(listener)
//        }
//        clickListeners.clear()
//        lastTapTimes.clear()
//    }
//}
