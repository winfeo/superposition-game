package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.LobbyResponse
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Lobby
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player

fun LobbyResponse.toDomain(): Lobby {
    return Lobby(
        players = players.map {
            Player(
                id = it.id,
                nickname = it.nickname
            )
        }
    )
}
