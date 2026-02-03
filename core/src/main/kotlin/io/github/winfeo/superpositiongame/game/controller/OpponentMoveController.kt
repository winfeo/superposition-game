package io.github.winfeo.superpositiongame.game.controller

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong

class OpponentMoveController {
    private var continuation: CancellableContinuation<Unit>? = null
    suspend fun makeMove() {
        delay(Random.nextLong(3000,7000))
        //TODO добавить простой скрипт выполнения (имитация хода игрока)
    }

    fun finishMove() {
        continuation?.resume(Unit)
        continuation = null
    }
}
