package io.github.winfeo.superpositiongame.android.domain.invitations

import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invite
import kotlinx.coroutines.flow.Flow

interface InvitationRepository {
    fun observeInvitations(userId: String): Flow<List<Invite>>
    suspend fun acceptInvitation(inviteId: String)
    suspend fun refuseInvitation(inviteId: String)
    suspend fun addListenerToInvitation(userId: String)
}
