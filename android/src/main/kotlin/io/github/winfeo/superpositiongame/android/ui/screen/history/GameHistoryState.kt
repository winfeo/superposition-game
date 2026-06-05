package io.github.winfeo.superpositiongame.android.ui.screen.history

import io.github.winfeo.superpositiongame.android.data.dto.rest.GameHistoryDTO

data class GameHistoryState(
    val isLoading: Boolean = false,
    val history: List<GameHistoryDTO> = emptyList(), //TODO переделать на Domain
    val error: String? = null
)
