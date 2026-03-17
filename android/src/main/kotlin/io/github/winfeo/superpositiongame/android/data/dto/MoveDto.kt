package io.github.winfeo.superpositiongame.android.data.dto

import io.github.winfeo.superpositiongame.model.game.GameMoveType

data class MoveDto(
    val type: GameMoveType = GameMoveType.PLAY_CARD,
    val playerId: String = "",
    val payload: Map<String, Any>? = null
)

