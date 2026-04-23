package io.github.winfeo.superpositiongame.android.ui.dialog

import io.github.winfeo.superpositiongame.model.dice.DiceState

sealed class GameDialogState {
    data class RotateDialog(
        val availableStates: List<DiceState>,
        val onStateSelected: (DiceState) -> Unit
    ): GameDialogState()
}
