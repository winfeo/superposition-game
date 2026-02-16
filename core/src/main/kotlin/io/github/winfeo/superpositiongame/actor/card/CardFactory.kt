package io.github.winfeo.superpositiongame.actor.card

import io.github.winfeo.superpositiongame.manager.CardsAtlasManager
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType

object CardFactory {

    private var idCounter = 0

    fun createRandomCard(): Card {

//        val textureName = CardsAtlasManager.getRandomCardId()
        val textureName =
            when (idCounter) {
                3 -> "swap"
                else -> CardsAtlasManager.getRandomCardId()
            }


        val type = CardType.entries.find {
            it.textureId == textureName
        }?: throw (IllegalStateException("Не удалось найти тип карты c id: $textureName"))

        return Card(
            id = "${textureName}_${idCounter++}",
            type = type
        )
    }

    fun createEmptyCard(): Card { ///TODO переделать на просто пустое место, а не пустую карту?
        return Card(
            id = "empty",
            type = CardType.EMPTY
        )
    }

    fun createDraggableCardForOpponentScript(): Card {
        val textureName = CardsAtlasManager.getRandomCardId()

        val type = CardType.entries.find {
            it.textureId == textureName
        }?: throw (IllegalStateException("Не удалось найти тип карты c id: $textureName"))

        if (!type.cardComponent.canDrag) {
            return createDraggableCardForOpponentScript()
        }

        return Card(
            id = "${textureName}_${idCounter++}",
            type = type
        )

    }

}
