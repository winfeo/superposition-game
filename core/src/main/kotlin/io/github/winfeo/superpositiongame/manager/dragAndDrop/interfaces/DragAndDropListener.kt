package io.github.winfeo.superpositiongame.manager.dragAndDrop.interfaces

import com.badlogic.gdx.scenes.scene2d.Actor

interface DragAndDropListener {
    fun onDragStarted(actor: Actor)
    fun onDragEnded(actor: Actor, success: Boolean)
    fun onDropSuccess(source: Actor, target: Actor)
    fun onValidationFailed(source: Actor, target: Actor, reason: String)
}
