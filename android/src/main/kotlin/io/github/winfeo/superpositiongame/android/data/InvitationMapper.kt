package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.InvitationDto
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

fun InvitationDto.toDomain(inviteId: String): Invitation {
    return Invitation(
        invitationId = inviteId,
        fromUserId = fromUserId
    )
}
