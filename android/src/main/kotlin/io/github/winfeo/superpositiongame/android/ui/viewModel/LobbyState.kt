package io.github.winfeo.superpositiongame.android.ui.viewModel

import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player

///TODO заменить на sealed interface?
data class LobbyState(
    val players: List<Player> = emptyList(), ///TODO content?
    val isLoading: Boolean = true,
    val error: String? = null
)
