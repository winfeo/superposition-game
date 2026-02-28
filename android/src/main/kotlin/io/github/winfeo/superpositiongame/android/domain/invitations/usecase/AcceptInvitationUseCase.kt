package io.github.winfeo.superpositiongame.android.domain.invitations.usecase

import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository

class AcceptInvitationUseCase(
    private val repository: InvitationRepository
) {
    suspend operator fun invoke(inviteId: String) {
        repository.acceptInvitation(inviteId)
    }
}
