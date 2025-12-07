package io.github.winfeo.superpositiongame.models

object CardTypeRepository {
    ///TODO подумать над тем как лучше оргинизовать хранение карты и добавить все карты
    private val cardsMap = mapOf(
        "bluecard1" to Card(
            id = "bluecard1",
            name = "Blue card 1",
            isFaceUp = true
        ),

        "redcard1" to Card (
            id = "redcard1",
            name = "Red card 1",
            isFaceUp = true
        ),

        "whitecard1" to Card (
            id = "whitecard1",
            name = "White card 1",
            isFaceUp = true
        ),

        "yellowcard1" to Card (
            id = "yellowcard1",
            name = "Yellow card 1",
            isFaceUp = true
        ),

        "greencard1" to Card (
            id = "greencard1",
            name = "Green card 1",
            isFaceUp = true
        ),
    )

    fun getCard(id: String): Card? = cardsMap[id]
}
