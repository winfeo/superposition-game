package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.actor.card.CardActor
import kotlin.math.PI
import kotlin.math.sin

class CardCircleLayout(
    private val stage: Stage
) {
    private val baseY = -60f
    private val fanHeight = 30f
    private val maxRotation = 15f

    fun layout(cards: List<CardActor>) {
        if (cards.isEmpty()) return

        val count = cards.size
        val stageWidth = stage.viewport.worldWidth
        val spacing = stageWidth / (count + 1)

        cards.forEachIndexed { index, card ->
            val x = spacing * (index + 1) - card.width / 2
            val t = index.toFloat() / (count - 1).coerceAtLeast(1)

            val yOffset = sin(t * PI).toFloat() * fanHeight
            val y = baseY + yOffset

            card.setOrigin(card.width / 2, 0f)
            card.setPosition(x, y)
            val rotation = (t - 0.5f) * 2 * maxRotation * -1
            card.rotation = rotation

            card.zIndex = 0
        }
    }
}
