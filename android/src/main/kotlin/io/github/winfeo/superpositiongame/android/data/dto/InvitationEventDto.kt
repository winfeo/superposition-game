package io.github.winfeo.superpositiongame.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InvitationEventDto(
    val type: String,
    val invitation: InvitationDto? = null,
    val invitations: List<InvitationDto>? = null
)
