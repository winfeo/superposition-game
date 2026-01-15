package io.github.winfeo.superpositiongame.managers.dragAndDrop.data

import com.badlogic.gdx.scenes.scene2d.Actor

//передача данных при перетаскивании
data class CardDragPayload(
    val sourceActor: Actor,
    var data: CardDragData
)
