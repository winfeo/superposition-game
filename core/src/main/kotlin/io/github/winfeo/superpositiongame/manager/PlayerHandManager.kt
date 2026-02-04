package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import io.github.winfeo.superpositiongame.actor.card.CardActor
import io.github.winfeo.superpositiongame.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.actor.card.CardFactory
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.doubleTap.GameTapController
import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameDragController
import io.github.winfeo.superpositiongame.ui.CardCircleLayout

class PlayerHandManager(
    private val stage: Stage,
    private val dragController: GameDragController,
    private val tapController: GameTapController
) {

    val cards = mutableListOf<CardActor>()
    private val layout = CardCircleLayout(stage)
    private val playerCardsAmount = GameConfig.getCardsInHandAmount()

    fun deal() {

        repeat(playerCardsAmount - cards.size) {
            val cardModel = CardFactory.createRandomCard()
            val cardActor = CardActorBuilder.createCardActorFromModel(cardModel)
            cardActor.setSize(cardActor.width * 1.5f,cardActor.height * 1.5f)
            stage.addActor(cardActor)

            if (cardActor.canDrag) dragController.setupCard(cardActor)
            else tapController.setupCard(cardActor)

            cards.add(cardActor)
        }

        cards.forEach { it.touchable = Touchable.enabled }

        layout.layout(cards)
    }

    fun removeCard(card: CardActor) {
        if (!cards.contains(card)) return
        cards.remove(card)
        card.remove()
        layout.layout(cards)
    }
}
