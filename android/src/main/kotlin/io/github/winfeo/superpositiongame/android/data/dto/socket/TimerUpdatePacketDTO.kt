package io.github.winfeo.superpositiongame.android.data.dto.socket

import kotlinx.serialization.Serializable

@Serializable
data class TimerUpdatePacketDTO(
    val timeLeftMs: Long,
    val serverTimestamp: Long
) {
}
