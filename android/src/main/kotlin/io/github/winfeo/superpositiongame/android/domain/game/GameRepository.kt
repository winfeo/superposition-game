package io.github.winfeo.superpositiongame.android.domain.game

import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun observeGameForUser(userId: String): Flow<String?>
}
