package io.github.winfeo.superpositiongame.android.ui.nav

import kotlinx.serialization.Serializable

@Serializable
data object LobbyRoute: AppRoute {
    override val route: String = "lobby"
}
