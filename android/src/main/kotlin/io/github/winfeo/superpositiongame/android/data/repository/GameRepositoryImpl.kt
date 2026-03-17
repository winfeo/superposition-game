package io.github.winfeo.superpositiongame.android.data.repository

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.github.winfeo.superpositiongame.android.data.dto.MoveDto
import io.github.winfeo.superpositiongame.android.data.toDomain
import io.github.winfeo.superpositiongame.android.data.toDto
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GameRepositoryImpl(
    database: FirebaseDatabase
): GameRepository {
    val ref = database.getReference("games")

    override fun observeGameForUser( userId: String): Flow<String?> {
        return callbackFlow {
            val listener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.children.firstOrNull { snapshot ->
                        val player1 = snapshot.child("players").child("player1").getValue(String::class.java)
                        val player2 = snapshot.child("players").child("player2").getValue(String::class.java)
                        Log.d("Debugg", "Repo. Player1: $player1, Player2: $player2")

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

    override suspend fun receiveMove(gameId: String): Flow<Move> {
        return callbackFlow {
            val listener = object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val dto = snapshot.getValue(MoveDto::class.java)
                    dto?.let { trySend(it.toDomain()) }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}

            }

            val movesRef = ref.child(gameId).child("moves")
            movesRef.addChildEventListener(listener)
            awaitClose { movesRef.removeEventListener(listener) }
        }
    }

    override suspend fun sendMove(
        gameId: String,
        move: Move
    ) {
        val dto = move.toDto()
        ref.child(gameId).child("moves").push().setValue(dto)
    }

    override suspend fun getPlayerIds(gameId: String): Flow<List<String>> { ///TODO убрать потом
        return callbackFlow {
            val playersRef = ref.child(gameId).child("players")
            val listener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ids = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                    trySend(ids)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }

            playersRef.addValueEventListener(listener)
            awaitClose { playersRef.removeEventListener(listener) }
        }
    }

}
