package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.Serializable

@Serializable
sealed class MoveDto{
    abstract val playerId: String
}
