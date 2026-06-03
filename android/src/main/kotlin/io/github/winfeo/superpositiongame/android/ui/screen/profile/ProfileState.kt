package io.github.winfeo.superpositiongame.android.ui.screen.profile

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.GameHistoryDTO

data class ProfileState(
    val isAuthorized: Boolean = false,
    val user: AuthorisedUserDTO? = null,
    val isLoadingHistory: Boolean = false,
    val gameHistory: List<GameHistoryDTO> = emptyList(),
    val historyError: String? = null
)
