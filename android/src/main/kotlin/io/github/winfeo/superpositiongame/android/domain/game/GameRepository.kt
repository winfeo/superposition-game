package io.github.winfeo.superpositiongame.android.domain.game

import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun observeGameForUser(userId: String): Flow<String?>
    suspend fun receiveMove(gameId: String): Flow<Move>
    suspend fun sendMove(gameId: String, move: Move)
    suspend fun getPlayerIds(gameId: String): Flow<List<String>> ///TODO убрать потом
}
