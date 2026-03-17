package io.github.winfeo.superpositiongame.model.game

enum class GamePhase(title: String) {
    WAITING_FOR_SECOND_PLAYER(title = "Waiting for opponent to join the game"),
    GAME_SETUP(title = "Game set up"),
    DEALING_CARDS(title = "Dealing cards"),
    PLAYER_TURN_BEGIN(title = "Player's turn"),
    PLAYER_TURN_END(title = "Player's turn"),
    FINISHED(title = "Game finished")
//    ANIMATED //TODO состояние для анимаций реализовать?
}
