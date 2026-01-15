package io.github.winfeo.superpositiongame.managers.dragAndDrop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import io.github.winfeo.superpositiongame.actors.CardActor
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.CardDragData
import io.github.winfeo.superpositiongame.managers.dragAndDrop.data.CardDragPayload
import io.github.winfeo.superpositiongame.managers.dragAndDrop.interfaces.DragAndDropListener
import io.github.winfeo.superpositiongame.managers.dragAndDrop.interfaces.DropValidation

//Логика перетаскивания карт
class CardsDragAndDropManager {
    ///TODO реализовать не через интерфейсы, а через колбэки?
    private val libgdxDragDrop = DragAndDrop()
    private val listeners = mutableListOf<DragAndDropListener>()
    private val validators = mutableMapOf<String, DropValidation>()
    private var currentPayload: CardDragPayload? = null

    fun addListener(listener: DragAndDropListener) {
        listeners.add(listener)
    }


    fun removeListener(listener: DragAndDropListener) {
        listeners.remove(listener)
    }

    fun registerValidator(type: String, validator: DropValidation) {
        validators[type] = validator
    }

    fun makeCardDraggable(
        card: Actor,
        sourceArea: GameAreas,
        cardData: Any? = null
    ) {

        val source = object : DragAndDrop.Source(card) {

            override fun dragStart(event: InputEvent, x: Float, y: Float, pointer: Int): DragAndDrop.Payload? {
                println("Отладка. Старт драга")
                val dragData = CardDragData(
                    card = (card as CardActor).card,
                    sourceArea = sourceArea
                )

                currentPayload = CardDragPayload(
                    sourceActor = card,
                    data = dragData
                )

                val payload = DragAndDrop.Payload()
                payload.`object` = currentPayload

                val dragVisual = createDragVisual(card)
                payload.dragActor = dragVisual

                card.color.a = 0.3f

                listeners.forEach { it.onDragStarted(card) }

                return payload
            }

            override fun dragStop(event: InputEvent, x: Float, y: Float, pointer: Int,
                                  payload: DragAndDrop.Payload?, target: DragAndDrop.Target?) {
                card.color.a = 1f

                val success = target != null
                listeners.forEach { it.onDragEnded(card, success) }

                currentPayload = null
            }
        }

        libgdxDragDrop.addSource(source)
    }

    fun makeDropTarget(target: Actor, validatorType: String = "default") {
        val targetObj = object : DragAndDrop.Target(target) {

            override fun drag(source: DragAndDrop.Source, payload: DragAndDrop.Payload,
                              x: Float, y: Float, pointer: Int): Boolean {
                val dragPayload = payload.`object` as? CardDragPayload ?: return false
                val validator = validators[validatorType] ?: return false
                val validation = validator.canAccept(dragPayload, target)
                val borderColor = validation.activeColor?: Color.WHITE
                target.color.set(borderColor)

                if (!validation.canPlace) {
                    validation.message?.let { message ->
                        listeners.forEach {
                            it.onValidationFailed(
                                source = dragPayload.sourceActor,
                                target = target,
                                reason = message
                            )
                        }
                    }
                }

                return validation.canPlace
            }

            override fun drop(source: DragAndDrop.Source, payload: DragAndDrop.Payload,
                              x: Float, y: Float, pointer: Int) {
                val dragPayload = payload.`object` as CardDragPayload
                target.color.set(1f, 1f, 1f, 1f)

                val validator = validators[validatorType]
                validator?.onDrop(dragPayload, target)

                listeners.forEach {
                    it.onDropSuccess(dragPayload.sourceActor, target)
                }
            }

            override fun reset(source: DragAndDrop.Source?, payload: DragAndDrop.Payload?) {
                target.color.set(1f, 1f, 1f, 1f)
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
        listeners.clear()
        validators.clear()
        currentPayload = null
    }
}
