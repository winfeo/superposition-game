package io.github.winfeo.superpositiongame.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InvitationEventDTO(
    val type: String,
    val invitation: InvitationDTO? = null,
    val invitations: List<InvitationDTO>? = null
)
