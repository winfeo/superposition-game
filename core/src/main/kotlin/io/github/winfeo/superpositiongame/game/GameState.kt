package io.github.winfeo.superpositiongame.game

enum class GameState(title: String) {
    GAME_SETUP(title = "Game set up"),
    DEALING_CARDS(title = "Dealing cards"),
    PLAYER_MOVE(title = "Your move"),
    OPPONENT_MOVE(title = "Opponents move"),
//    ANIMATED //TODO состояние для анимаций реализовать?
}
