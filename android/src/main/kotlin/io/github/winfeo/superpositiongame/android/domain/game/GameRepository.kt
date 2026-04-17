package io.github.winfeo.superpositiongame.android.domain.game

import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.flow.Flow

interface GameRepository {
//    fun observeGameForUser(userId: String): Flow<String?>
//    suspend fun receiveMove(gameId: String): Flow<Move>
    suspend fun sendMove(gameId: String, move: Move)
//    suspend fun getPlayerIds(gameId: String): Flow<List<String>> ///TODO убрать потом
    fun observeGameState(gameId: String, playerId: String): Flow<GameState> ///TODO убрать playerId (решить проблему владельца слота)
    fun observeGameStart(): Flow<String>
}
