package io.github.winfeo.superpositiongame.model.game

import io.github.winfeo.superpositiongame.model.dice.DiceState

//все команды, которые меняют состояние игры
//действия игрока в игре
sealed class Move {
    abstract val playerId: String
    abstract val type: GameMoveType

//    data class StartGame(
//        override val type: GameMoveType = GameMoveType.START_GAME,
//        override val playerId: String, //Отправляет первый игрок команду?
//        val playerRandomDices: Map<String, List<DiceState>>,
//        val playerRequiredDices: Map<String, List<DiceState>>
//    ): Move()

    data class PlayCard(
        override val type: GameMoveType = GameMoveType.PLAY_CARD,
        override val playerId: String,
        val cardId: String,
        val targetSlotIndex: Int,
        val targetPlayerId: String
    ): Move()

    data class RotateDice(
        override val type: GameMoveType = GameMoveType.ROTATE_DICE,
        override val playerId: String,
        val cardId: String,
        val targetSlotIndex: Int,
        val newState: DiceState,
        val targetPlayerId: String
    ): Move()

    data class SwapDices(
        override val type: GameMoveType = GameMoveType.SWAP_DICES,
        override val playerId: String,
        val firstSlotIndex: Int,
        val secondSlotIndex: Int
    ): Move()

//    data class BeginTurn(
//        override val type: GameMoveType = GameMoveType.BEGIN_TURN,
//        override val playerId: String
//    ): Move()

//    data class EndTurn(
//        override val type: GameMoveType = GameMoveType.END_TURN,
//        override val playerId: String
//    ): Move()

//    data class DealCards(
//        override val type: GameMoveType = GameMoveType.DEAL_CARDS,
//        override val playerId: String,
//        val playersNewCards: Map<String, List<String>>
//    ): Move()

    //Игрок выйграл
    /*data class FinishGame(
        val type: String,
        override val playerId: String
    ): Move()*/
}
