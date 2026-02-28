package io.github.winfeo.superpositiongame.android.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.github.winfeo.superpositiongame.android.data.dto.InvitationDto
import io.github.winfeo.superpositiongame.android.data.toDomain
import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class InvitationRepositoryImpl(
    private val database: FirebaseDatabase
): InvitationRepository {
    ///TODO создать Firebase репоизторий, переделать потом

    val ref = database.getReference("invitations")
    val refGames = database.getReference("games")

    override fun observeInvitations(userId: String): Flow<List<Invitation>> {
        return callbackFlow {
            val listener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children
                        .mapNotNull { child ->
                            val dto = child.getValue(InvitationDto::class.java)
                            if(dto?.toUserId == userId) {
                                dto.toDomain(child.key!!)
                            }
                            else null
                        }
                    trySend(list)
                }

                override fun onCancelled(error: DatabaseError) {}
            }

            ref.addValueEventListener(listener)

            awaitClose {
                ref.removeEventListener(listener)
            }
        }
    }

    override suspend fun acceptInvitation(inviteId: String) {
        val inviteSnapshot = ref.child(inviteId).get().await()
        val fromUserId = inviteSnapshot.child("fromUserId").getValue(String::class.java)
        val toUserId = inviteSnapshot.child("toUserId").getValue(String::class.java)
        val gameId = refGames.push().key?: return

        refGames.child(gameId).setValue(
            mapOf(
                "player1" to fromUserId,
                "player2" to toUserId,
                "status" to "Set up game" ///TODO сделать энам?
            )
        )

        ref.child(inviteId).removeValue() ///TODO удалять все приглашение которые были тоже?
    }

    override suspend fun refuseInvitation(inviteId: String) {
    }

    override suspend fun addListenerToInvitation(userId: String) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { inviteSnapshot ->
                    val fromUserId = inviteSnapshot.child("fromUserId").getValue(String::class.java)
                    val toUserId = inviteSnapshot.child("toUserId").getValue(String::class.java)
                    if (userId == fromUserId || userId == toUserId) {
                        inviteSnapshot.ref.onDisconnect().removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
