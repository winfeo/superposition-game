package io.github.winfeo.superpositiongame.android.domain.invitations.usecase

import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository

class AddListenerToInvitationUseCase(
    private val repository: InvitationRepository
) {
    suspend operator fun invoke(userId: String) {
        repository.addListenerToInvitation(userId)
    }
}
