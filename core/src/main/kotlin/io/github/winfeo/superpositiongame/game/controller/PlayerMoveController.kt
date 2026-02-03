package io.github.winfeo.superpositiongame.game.controller

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayerMoveController {
    private var continuation: CancellableContinuation<Unit>? = null

    suspend fun makeMove() = suspendCancellableCoroutine { cont ->
        continuation = cont
    }

    fun finishMove() {
        continuation?.resume(Unit)
        continuation = null
    }
}
