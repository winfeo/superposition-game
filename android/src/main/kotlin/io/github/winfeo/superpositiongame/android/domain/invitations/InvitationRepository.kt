package io.github.winfeo.superpositiongame.android.domain.invitations

import io.github.winfeo.superpositiongame.android.data.dto.InvitationDto
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation
import kotlinx.coroutines.flow.Flow

interface InvitationRepository {
    fun observeInvitations(userId: String): Flow<List<Invitation>>
    suspend fun acceptInvitation(invitation: InvitationDto)
    suspend fun rejectInvitation(invitation: InvitationDto)
}
