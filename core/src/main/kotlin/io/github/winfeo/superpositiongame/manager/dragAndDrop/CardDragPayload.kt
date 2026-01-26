package io.github.winfeo.superpositiongame.manager.dragAndDrop

import com.badlogic.gdx.scenes.scene2d.Actor

//передача данных при перетаскивании
data class CardDragPayload(
    val sourceActor: Actor
)
