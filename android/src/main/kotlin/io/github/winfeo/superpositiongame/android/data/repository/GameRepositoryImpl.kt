package io.github.winfeo.superpositiongame.android.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GameRepositoryImpl(
    private val database: FirebaseDatabase
): GameRepository {
    val ref = database.getReference("games")

    override fun observeGameForUser(userId: String): Flow<String?> {
        return callbackFlow {
            val listener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.children.firstOrNull() { snapshot ->
                        val player1 = snapshot.child("player1").getValue(String::class.java)
                        val player2 = snapshot.child("player2").getValue(String::class.java)
                        player1 == userId || player2 == userId
                    }

                    trySend(game?.key).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }

            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }
    }
}
