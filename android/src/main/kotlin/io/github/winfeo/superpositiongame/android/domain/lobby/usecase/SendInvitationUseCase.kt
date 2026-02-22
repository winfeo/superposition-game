package io.github.winfeo.superpositiongame.android.domain.lobby.usecase

import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository

class SendInvitationUseCase(
    private val repository: LobbyRepository
) {
    suspend operator fun invoke(fromUser: String, toUser: String) {
        repository.sendInvitation(fromUser, toUser)
    }
}
