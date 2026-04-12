package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.InvitationDto
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

fun InvitationDto.toDomain(): Invitation {
    return Invitation(
        senderId = senderId,
        receiverId = receiverId,
        sendTime = sendTime
    )
}

fun Invitation.toDto(
    currentUserId: String
): InvitationDto {
    return InvitationDto(
        senderId = senderId,
        receiverId = currentUserId,
        sendTime = sendTime
    )
}
