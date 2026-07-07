package io.github.winfeo.superpositiongame.android.domain.game

import io.github.winfeo.superpositiongame.android.domain.game.model.TimerUpdatePacket
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun sendMove(gameId: String, move: Move)
    fun observeGameState(gameId: String, playerId: String): Flow<GameState>
    fun observeGameStart(): Flow<String>
    fun observeTimerUpdates(gameId: String): Flow<TimerUpdatePacket>
}
