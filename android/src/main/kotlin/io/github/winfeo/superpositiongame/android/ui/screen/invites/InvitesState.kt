package io.github.winfeo.superpositiongame.android.ui.screen.invites

import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invite

data class InvitesState (
    val invites: List<Invite> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
