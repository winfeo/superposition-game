package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.GameHistoryDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.GameHistoryApi

class GameHistoryRepository(
    private val api: GameHistoryApi
) {
    suspend fun getGameHistory(userId: Long): Result<List<GameHistoryDTO>> {
        return try {
            val history = api.getGameHistory(userId)
            Result.success(history)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
