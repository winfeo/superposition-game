package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.actor.card.CardActor
import kotlin.math.PI
import kotlin.math.sin

class CardCircleLayout(
    private val stage: Stage
) {
    private val baseY = -25f       // Нижняя линия
    private val fanHeight = 30f    // Высота дуги
    private val maxRotation = 15f  // Максимальный наклон карт наружу

    fun layout(cards: List<CardActor>) {
        if (cards.isEmpty()) return

        val count = cards.size
        val stageWidth = stage.viewport.worldWidth
        val spacing = stageWidth / (count + 1)  // Равномерное распределение по X

        cards.forEachIndexed { index, card ->
            val x = spacing * (index + 1) - card.width / 2

            // Нормализованный индекс 0..1
            val t = index.toFloat() / (count - 1).coerceAtLeast(1)

            // Y-координата: низ карт на плавной дуге
            val yOffset = sin(t * PI).toFloat() * fanHeight
            val y = baseY + yOffset

            // Устанавливаем origin в центр карты для правильного вращения
            card.setOrigin(card.width / 2, 0f) // x-центр, y-низ карты

            card.setPosition(x, y)

            // Наклон карты наружу от центра
            val rotation = (t - 0.5f) * 2 * maxRotation * -1
            card.rotation = rotation

            card.zIndex = 0
        }
    }
}
