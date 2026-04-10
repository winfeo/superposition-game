package io.github.winfeo.superpositiongame.android.domain.lobby.usecase

import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository
import io.github.winfeo.superpositiongame.android.domain.lobby.model.User
import kotlinx.coroutines.flow.Flow

class ObserveUsersUseCase(
    private val repository: LobbyRepository,
    private val currentUserId: String
) {
    operator fun invoke(): Flow<List<User>> {
        return repository.observeUsersInLobby(currentUserId)
    }
}
