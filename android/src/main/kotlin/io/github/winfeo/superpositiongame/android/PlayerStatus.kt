package io.github.winfeo.superpositiongame.android

//Статусы пользователя при использовании приложения
enum class PlayerStatus(val title: String) {
    IN_LOBBY(title = "searching for a game"),
    IN_GAME(title = "in a game"),
    DISCONNECTED(title = "disconnected from app")
}
