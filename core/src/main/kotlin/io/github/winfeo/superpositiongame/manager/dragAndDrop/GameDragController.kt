package io.github.winfeo.superpositiongame.manager.dragAndDrop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.SlotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

//Контроллер (слушатель) перетаскивания карт
class GameDragController(
    private val stage: Stage
) {

    private val dragManager = CardsDragAndDropManager(this)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        setupValidators()
    }

    fun setupCard(card: CardActor) {
        dragManager.makeCardDraggable(card)
    }

    fun setupSlot(slot: SlotActor, validatorType: String = "slot") {
        dragManager.makeDropTarget(slot, validatorType)
    }

    private fun setupValidators() {
        val dropValidator = DropValidator(stage, scope)
        dragManager.registerValidator("slot", dropValidator)
        dragManager.registerValidator("attack_slot", dropValidator)
    }

    fun onDragStarted(actor: Actor) {
        println("Отладка. Начали перетаскивать: ${(actor as CardActor).card.id}")
    }

    fun onDragEnded(actor: Actor, success: Boolean) {
        println("Отладка. Закончили перетаскивать, результат: $success")
    }

    fun onDropSuccess(source: Actor, target: Actor) {
        println("Отладка. Успешный дроп: ${(source as CardActor).card.id} -> ${(target as SlotActor).getCard().card.id}")
    }

    fun onValidationFailed(source: Actor, target: Actor, reason: String) {
        println("Отладка. Не удалось сбросить: $reason")
    }

    fun dispose() {
        dragManager.clear()
        scope.cancel()
    }
}
