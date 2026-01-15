package io.github.winfeo.superpositiongame.managers.dragAndDrop

import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.winfeo.superpositiongame.actors.CardActor
import io.github.winfeo.superpositiongame.actors.SlotActor
import io.github.winfeo.superpositiongame.managers.dragAndDrop.interfaces.DragAndDropListener
import io.github.winfeo.superpositiongame.managers.dragAndDrop.validation.AttackSlotValidation
import io.github.winfeo.superpositiongame.managers.dragAndDrop.validation.SlotValidation

class GameDragController: DragAndDropListener {

    private val dragManager = CardsDragAndDropManager()

    init {
        dragManager.addListener(this)
        setupValidators()
    }

    fun setupCard(card: CardActor, area: GameAreas) {
        dragManager.makeCardDraggable(card, area)
    }

    fun setupSlot(slot: SlotActor, validatorType: String = "slot") {
        dragManager.makeDropTarget(slot, validatorType)
    }

    private fun setupValidators() {
        dragManager.registerValidator("slot", SlotValidation())
        dragManager.registerValidator("attack_slot", AttackSlotValidation())
    }

    override fun onDragStarted(actor: Actor) {
        println("Отладка. Начали перетаскивать: ${actor.javaClass.simpleName}")
    }

    override fun onDragEnded(actor: Actor, success: Boolean) {
        println("Отладка. Закончили перетаскивать: $success")
    }

    override fun onDropSuccess(source: Actor, target: Actor) {
        println("Отладка. Успешный дроп: ${source.javaClass.simpleName} -> ${target.javaClass.simpleName}")
    }

    override fun onValidationFailed(source: Actor, target: Actor, reason: String) {
        println("Отладка. Не удалось сбросить: $reason")
    }

    fun dispose() {
        dragManager.clear()
    }
}
