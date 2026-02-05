package io.github.winfeo.superpositiongame.model.dice

data class Dice(
    val id: String,
    val state: DiceState,
    val requiredState: DiceState? = null
) {
    fun isInRequiredState(): Boolean {
        return requiredState == state
    }
}
