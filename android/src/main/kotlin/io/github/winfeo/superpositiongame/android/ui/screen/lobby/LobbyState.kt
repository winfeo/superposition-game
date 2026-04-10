package io.github.winfeo.superpositiongame.android.ui.screen.lobby

import io.github.winfeo.superpositiongame.android.domain.lobby.model.User

///TODO заменить на sealed interface?
data class LobbyState(
    val users: List<User> = emptyList(), ///TODO content?
    val isLoading: Boolean = true,
    val error: String? = null
)
