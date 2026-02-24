package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.InvitationDto
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invite

fun InvitationDto.toDomain(inviteId: String): Invite {
    return Invite(
        fromUserId = fromUserId
    )
}
