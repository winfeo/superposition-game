package io.github.winfeo.superpositiongame.android.data

import io.github.winfeo.superpositiongame.android.data.dto.LobbyResponse
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Lobby
import io.github.winfeo.superpositiongame.android.domain.lobby.model.User

fun LobbyResponse.toDomain(): Lobby {
    return Lobby(users = users.map { User(it.id) })
}
