package io.github.winfeo.superpositiongame.android.ui.dialog

import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState

sealed class GameDialogState {
    data class RotateDialog(
        val availableStates: List<DiceState>,
        val onStateSelected: (DiceState) -> Unit
    ): GameDialogState()

    data class ReshuffleDialog(
        val cards: List<Card>,
        val maxSelectable: Int = 4,
        val minSelectable: Int = 1,
        val onCardsSelected: (List<Card>) -> Unit
    ): GameDialogState()

    data class CardPreviewDialog(
        val card: Card
    ): GameDialogState()

    data class GameFinishedDialog(
        val isWinner: Boolean,
        val onReturnToLobby: () -> Unit
    ): GameDialogState()

    data class GameMenuDialog(
        val onResume: () -> Unit,
        val onRules: () -> Unit,
        val onSettings: () -> Unit,
        val onSurrender: () -> Unit,
        val onDismiss: () -> Unit
    ): GameDialogState()
}
