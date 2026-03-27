package io.github.winfeo.superpositiongame.model.card

import io.github.winfeo.superpositiongame.manager.CardsAtlasManager

object CardFactory {
    private val cardRepository: List<String> = listOf(
        "pauli_x",
        "pauli_y",
        "pauli_z",
        "pauli_x3",
        "pauli_y3",
        "pauli_z3",
        "rotate_x",
        "rotate_y",
        "rotate_z",
        "phase_s",
        "phase_s_backwards",
        "hadamard_h",
        "hadamard_h3",
        "swap",
        "quantum_noise",
        "kronecker_multiplication",
        "measurement",
        "identity"
    )
    private var idCounter = 0

    fun createRandomCard(): Card {
        val textureName = CardsAtlasManager.getRandomCardId()
        return buildCard(textureName)
    }

    fun createCardFromName(cardName: String): Card = buildCard(cardName)

    private fun buildCard(textureName: String): Card {
        val description = CardRepository.getDescription(textureName)?: throw (IllegalStateException("Не удалось найти тип карты c id: $textureName"))
        return Card(
            id = "${textureName}_${idCounter++}",
            textureId = textureName,
            description = description
        )
    }

    fun getRandomCardName(): String = cardRepository.random()

//    fun createEmptyCard(): Card { ///TODO переделать на просто пустое место, а не пустую карту?
//        return Card(
//            id = "empty",
//            textureId = null,
//            description = cardRepository["empty"]!!
//        )
//    }

//    fun createDraggableCardForOpponentScript(): Card {
//        val textureName = CardsAtlasManager.getRandomCardId()
//
//        val type = CardType.entries.find {
//            it.textureId == textureName
//        }?: throw (IllegalStateException("Не удалось найти тип карты c id: $textureName"))
//
//        if (!type.cardComponent.canDrag) {
//            return createDraggableCardForOpponentScript()
//        }
//
//        return Card(
//            id = "${textureName}_${idCounter++}",
//            type = type
//        )
//
//    }

}
