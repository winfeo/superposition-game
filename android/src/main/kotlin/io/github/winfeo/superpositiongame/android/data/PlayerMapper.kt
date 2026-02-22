package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.PlayerDto
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player

fun PlayerDto.toDomain(id: String): Player {
    return Player(
        id = id,
        createdAt = createdAt,
        status = status
    )
}
