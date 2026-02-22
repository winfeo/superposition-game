package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.InvitationDto
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

fun InvitationDto.toDomain(id: String): Invitation {
    return Invitation(
        id = id,
        fromUserId = fromUserId
    )
}
