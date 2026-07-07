package io.github.winfeo.superpositiongame.android.domain.game.model

data class TimerUpdatePacket(
    val timeLeftMs: Long,
    val serverTimestamp: Long
)
