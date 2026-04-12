package io.github.winfeo.superpositiongame.android.domain.invitations.usecase

import io.github.winfeo.superpositiongame.android.data.toDto
import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

class RejectInvitationUseCase(
    private val repository: InvitationRepository
) {
    suspend operator fun invoke(
        invitation: Invitation,
        currentUserId: String
    ) {
        val dto = invitation.toDto(currentUserId)
        repository.rejectInvitation(dto)
    }
}
