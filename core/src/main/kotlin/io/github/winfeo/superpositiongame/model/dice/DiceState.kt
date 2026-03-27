package io.github.winfeo.superpositiongame.model.dice

//Возможные состояния кубитов
enum class DiceState(
    val stateName: String,
    val textureId: String
) {
    ZERO(
        stateName = "\"0\" state",
        textureId = "zero",
    ),
    ONE(
        stateName = "\"1\" state",
        textureId = "one",
    ),
    PLUS(
        stateName = "\"+\" state",
        textureId = "plus",
    ),
    MINUS(
        stateName = "\"-\" state",
        textureId = "minus",
    ),
    I(
        stateName = "\"I\" state",
        textureId = "i_plus",
    ),
    I_MINUS(
        stateName = "\"-I\" state",
        textureId = "i_minus",
    )
}
