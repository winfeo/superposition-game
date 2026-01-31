package io.github.winfeo.superpositiongame.manager.tap

import com.badlogic.gdx.scenes.scene2d.ui.Image
import io.github.winfeo.superpositiongame.actor.card.CardActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import ktx.actors.alpha

//Контроллер (слушатель) использования специальных карт (двойное нажатие на них)
class GameTapController() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    ///TODO подумать над тем, чтобы не передавать скоуп как параметр
    private val touchManager = CardsTapManager(this, scope)
    private val tapValidator = TapValidator(scope = scope)
    fun setupCard(card: CardActor) {
        touchManager.makeCardTouchable(card)
    }


    fun onCardTapped(card: CardActor) {
        println("Отладка. Нажали на специальную карту: ${card.card.id}")
    }

    fun onCardDoubleTapped(card: CardActor) {
        println("Отладка. Активируем специальную карту: ${card.card.id}")
        tapValidator.onAccept(card)
    }

    fun dispose() {
        touchManager.clear()
        scope.cancel()
    }

}
