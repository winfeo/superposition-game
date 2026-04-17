package io.github.winfeo.superpositiongame.ui.screen.elements

import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.CardsDragAndDropManager
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
import io.github.winfeo.superpositiongame.ui.actor.card.CardActorBuilder
import kotlin.math.PI
import kotlin.math.sin

//Веер-карт игрока
class CardsFan(
    private val playerId: String,
    private val stage: Stage,
    private val dragManager: CardsDragAndDropManager
) {
    private val cardActors = mutableListOf<CardActor>()
    private val baseY = -60f ///TODO переделать настройку (динамически от размера экрана сделать)
    private val fanHeight = 30f
    private val maxRotation = 15f

    fun render(state: GameState) {
        val cards = state.players[playerId]?.hand?: return
        syncActors(cards)
    }

    private fun syncActors(cards: List<Card>) {
//        if (cardActors.size == cards.size) return
        if (cardActors.hashCode() == cards.hashCode()) return

        clearActors()
        cards.forEach { card ->
            val actor = CardActorBuilder.buildCardActor(card)
            ///TODO не все карты перетаскиваемые, какие-то Touchable. Переделать
            dragManager.makeCardDraggable(actor)
            stage.addActor(actor)
            cardActors.add(actor)
        }

        renderFan(cardActors)
    }

    private fun clearActors() {
        cardActors.forEach { it.remove() }
        cardActors.clear()
    }

    private fun renderFan(cards: List<CardActor>) {
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
