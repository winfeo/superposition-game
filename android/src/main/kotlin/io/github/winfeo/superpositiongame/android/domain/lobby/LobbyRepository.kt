package io.github.winfeo.superpositiongame.android.domain.lobby

import io.github.winfeo.superpositiongame.android.domain.lobby.model.User
import kotlinx.coroutines.flow.Flow

interface LobbyRepository {
    fun observeUsersInLobby(currentUserId: String): Flow<List<User>>
    suspend fun sendInvitation(fromUserId: String, toUserId: String)
}
