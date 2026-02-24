package io.github.winfeo.superpositiongame.android.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.github.winfeo.superpositiongame.android.data.dto.PlayerDto
import io.github.winfeo.superpositiongame.android.data.toDomain
import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LobbyRepositoryImpl(
    private val database: FirebaseDatabase
): LobbyRepository {
    private val usersRef = database.getReference("users")
    private val invitesRef = database.getReference("invitations")

    override fun observePlayersInLobby(currentUserId: String): Flow<List<Player>> {
        return callbackFlow {
            val listener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val players = snapshot.children.mapNotNull { child ->
                        val dto = child.getValue(PlayerDto::class.java)
                        dto?.toDomain(child.key!!)
                    }.filter { child -> child.id != currentUserId }
                    trySend(players)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }

            usersRef.addValueEventListener(listener)
            awaitClose { usersRef.removeEventListener(listener) }
        }
    }

    override suspend fun sendInvitation(fromUserId: String, toUserId: String) {
        val inviteId = invitesRef.push().key ?: return

        invitesRef.child(inviteId).setValue(mapOf(
            "fromUserId" to fromUserId,
            "toUserId" to toUserId
        ))
    }

}
