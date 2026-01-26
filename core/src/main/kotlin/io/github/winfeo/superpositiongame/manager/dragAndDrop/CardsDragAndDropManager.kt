package io.github.winfeo.superpositiongame.manager.dragAndDrop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.SlotActorStates
import io.github.winfeo.superpositiongame.rules.model.ValidationResult

//Логика перетаскивания карт в слоты
class CardsDragAndDropManager(
    currentListener: GameDragController
) {
    private val libgdxDragDrop = DragAndDrop()
    private val listener: GameDragController = currentListener
    private val validators = mutableMapOf<String, DropValidator>()
    private var currentPayload: CardDragPayload? = null


    fun registerValidator(type: String, validator: DropValidator) {
        validators[type] = validator
    }

    fun makeCardDraggable(
        card: Actor
    ) {

        val source = object : DragAndDrop.Source(card) {

            override fun dragStart(event: InputEvent, x: Float, y: Float, pointer: Int): DragAndDrop.Payload {
                println("Отладка. Старт драга. Взяли за: x=$x y=$y")

                ///TODO переделать, просто карту передавать как пэйлоуд?
                currentPayload = CardDragPayload(
                    sourceActor = card
                )

                val payload = DragAndDrop.Payload()
                payload.`object` = currentPayload

                val dragVisual = createDragVisual(card)
                payload.dragActor = dragVisual

                libgdxDragDrop.setDragActorPosition(card.width - x, -y)

                card.color.a = 0.3f

                listener.onDragStarted(card)

                return payload
            }

            override fun dragStop(event: InputEvent, x: Float, y: Float, pointer: Int,
                                  payload: DragAndDrop.Payload?, target: DragAndDrop.Target?) {
                card.color.a = 1f

                val success = target != null
                listener.onDragEnded(card, success)

                currentPayload = null
            }
        }

        libgdxDragDrop.addSource(source)
    }

    fun makeDropTarget(target: SlotActor, validatorType: String = "default") {
        val targetObj = object : DragAndDrop.Target(target) {

            override fun drag(source: DragAndDrop.Source, payload: DragAndDrop.Payload,
                              x: Float, y: Float, pointer: Int): Boolean {
                val dragPayload = payload.`object` as? CardDragPayload ?: return false
                val validator: DropValidator = validators[validatorType] ?: return false
                val validation: ValidationResult = validator.canAccept(dragPayload, target)

                target.setState(validation.activeState)

                if (!validation.canPlace) {
                    validation.message?.let { message ->
                        listener.onValidationFailed(
                            source = dragPayload.sourceActor,
                            target = target,
                            reason = message
                        )
                    }
                }

                return validation.canPlace
            }

            override fun drop(source: DragAndDrop.Source, payload: DragAndDrop.Payload,
                              x: Float, y: Float, pointer: Int) {
                val dragPayload = payload.`object` as CardDragPayload

                val validator = validators[validatorType]
                validator?.onDrop(dragPayload, target)

                listener.onDropSuccess(
                    source = dragPayload.sourceActor,
                    target = target
                )
            }

            override fun reset(source: DragAndDrop.Source?, payload: DragAndDrop.Payload?) {
                target.setState(SlotActorStates.NO_ACTION)
            }
        }

        libgdxDragDrop.addTarget(targetObj)
    }

    private fun createDragVisual(original: Actor): Actor {
        val image = original as CardActor
        return Image(image.drawable).apply {
            setSize(image.width, image.height)
            color.a = 0.7f
        }
    }

    fun clear() {
        libgdxDragDrop.clear()
        listener.dispose()
        validators.clear()
        currentPayload = null
    }
}
