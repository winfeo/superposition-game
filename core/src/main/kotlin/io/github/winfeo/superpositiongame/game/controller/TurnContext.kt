package io.github.winfeo.superpositiongame.game.controller

import io.github.winfeo.superpositiongame.actor.SlotArea

object TurnContext {
    var remainingMoves: Int = 1
    var lockedArea: SlotArea? = null
    var isMultiplicationActive: Boolean = false

    fun reset() {
        remainingMoves = 1
        lockedArea = null
        isMultiplicationActive = false
    }
}
