//package io.github.winfeo.superpositiongame.game
//
//import io.github.winfeo.superpositiongame.model.game.SlotOwner
//import io.github.winfeo.superpositiongame.model.game.SlotState
//import io.github.winfeo.superpositiongame.model.card.CardFactory
//import io.github.winfeo.superpositiongame.model.dice.DiceFactory
//import io.github.winfeo.superpositiongame.game.effect.CardEffectsRepository
//import io.github.winfeo.superpositiongame.config.GameConfig
//import io.github.winfeo.superpositiongame.model.dice.DiceState
//import io.github.winfeo.superpositiongame.model.game.GamePhase
//import io.github.winfeo.superpositiongame.model.game.GameState
//import io.github.winfeo.superpositiongame.model.game.Move
//
////Обрабатывает входящую команду Move и обновляет состояние игры
/////TODO вынести команды в отдельные классы тоже. интерфейс?
//class GameEngine {
//
//    fun applyMove(
//        currentState: GameState,
//        move: Move
//    ): GameState {
//        if (move !is Move.StartGame && currentState.currentPlayerId != move.playerId) {
//            return currentState //Старт игры или TODO если не ход игрока, или просто блокировать UI?
//        }
//
//        ///TODO когда играем Kronecker Multiplication менять и сбрасывать в null GameState.activeSlotsRow
//        val newState = when(move) {
//            is Move.PlayCard -> { handlePlayCard(currentState, move) }
//            is Move.RotateDice -> { handleRotateDice(currentState, move) }
//            is Move.SwapDices -> { handleSwapDices(currentState, move) }
//
//            //Вспомогательные команды
//            is Move.StartGame -> { handleGameStarted(currentState, move) }
//            is Move.BeginTurn -> { handleBeginTurn(currentState, move) }
//            is Move.EndTurn -> { handleEndTurn(currentState, move) }
//            is Move.DealCards -> { handleDealtCards(currentState, move) }
//            //is Move.FinishGame -> { handleFinishGame(currentState, move) }
//        }
//
//        return checkVictory(newState, move.playerId)
//    }
//
//}
//
//private fun handleBeginTurn(
//    currentState: GameState,
//    move: Move.BeginTurn
//): GameState {
//    return currentState.copy(
//        currentPlayerId = move.playerId,
//        turnNumber = currentState.turnNumber + 1
//    )
//    ///TODO запускать таймер?
//}
//
//private fun handleEndTurn(
//    currentState: GameState,
//    move: Move.EndTurn
//): GameState {
//    val nextPlayerId = currentState.players.keys.first { it != move.playerId }
//    return currentState.copy(
//        currentPlayerId = nextPlayerId,
//        turnNumber = currentState.turnNumber + 1
//    )
//}
//
//private fun handleDealtCards(
//    currentState: GameState,
//    move: Move.DealCards
//): GameState {
//    val updatedPlayers = currentState.players.mapValues { (playerId, playerState) ->
//        val newCardsNames = move.playersNewCards[playerId]?: emptyList()
//        val newCards = newCardsNames.map { CardFactory.createCardFromName(it) }
//        ///TODO переписать
//        playerState.copy(hand = playerState.hand + newCards.take(GameConfig.getCardsInHandAmount() - playerState.hand.size))
//    }
//
//    return currentState.copy(
//        phase = GamePhase.PLAYER_TURN_BEGIN,
//        currentPlayerId = move.playerId,
//        players = updatedPlayers,
//        turnNumber = currentState.turnNumber + 1
//    )
//}
//
//private fun handleGameStarted(
//    currentState: GameState,
//    move: Move.StartGame
//): GameState {
//    val updatedPlayers = currentState.players.mapValues { (playerId, playerState) ->
//        val playerRandomDices = move.playerRandomDices[playerId]!!
//        println("Random: $playerRandomDices")
//        val playerRequiredDices = move.playerRequiredDices[playerId]!!
//        println("Required: $playerRandomDices")
//        val slotOwner = if (playerId == move.playerId) SlotOwner.PLAYER else SlotOwner.OPPONENT
//
//        val slots = mutableListOf<SlotState>()
//        repeat(GameConfig.getSlotsOnTableAmount()) { index ->
//            val dice = DiceFactory.createDiceFromStates(
//                diceState = playerRandomDices[index],
//                requiredState = playerRequiredDices[index]
//            )
//
//            slots.add(
//                SlotState(
//                    index = index,
//                    slotOwner = slotOwner,
//                    initialDice = dice,
//                    dice = dice
//                )
//            )
//        }
//
//        playerState.copy(slots = slots)
//    }
//
//    return currentState.copy(
//        phase = GamePhase.DEALING_CARDS,
//        currentPlayerId = move.playerId,
//        players = updatedPlayers,
//        turnNumber = currentState.turnNumber + 1
//    )
//}
//
//private fun checkVictory(
//    newState: GameState,
//    playerId: String
//): GameState {
//    val isWinner = newState.players[playerId]!!.slots.all { slot ->
//        val dice = slot.dice
//        dice.state == dice.requiredState
//    }
//
//    return if (isWinner) { newState.copy(phase = GamePhase.FINISHED) }
//    else newState
//}
//
//private fun handlePlayCard(
//    currentState: GameState,
//    move: Move.PlayCard
//): GameState {
//    val actingPlayer = currentState.players[move.playerId]?: return currentState
//    val card = actingPlayer.hand.find { it.id == move.cardId }?: return currentState
//    val effect = CardEffectsRepository.getEffect(card.type)?: return currentState
//
//    val stateAfterEffect = effect.apply(
//        state = currentState,
//        card = card,
//        targetSlotIndex = move.targetSlotIndex,
//        playerId = move.targetPlayerId
//    )
//
//    val updatedActingPlayer = stateAfterEffect.players[move.playerId]!!.copy(
//        hand = stateAfterEffect.players[move.playerId]!!.hand.filter { it.id != move.cardId }
//    )
//
//    val updatedPlayers = stateAfterEffect.players.toMutableMap()
//    updatedPlayers[move.playerId] = updatedActingPlayer
//
//    return stateAfterEffect.copy(players = updatedPlayers)
//}
//
//private fun handleRotateDice(
//    currentState: GameState,
//    move: Move.RotateDice
//): GameState {
//    val targetPlayer = currentState.players[move.targetPlayerId] ?: return currentState
//    val actingPlayer = currentState.players[move.playerId] ?: return currentState
//    val slots = targetPlayer.slots.toMutableList()
//    val slot = slots[move.targetSlotIndex]
//    val card = actingPlayer.hand.find { it.id == move.cardId } ?: return currentState
//
//    slots[move.targetSlotIndex] = slot.copy(
//        dice = slot.dice.copy(state = move.newState),
//        appliedCards = slot.appliedCards + card
//    )
//
//    val updatedTargetPlayer = targetPlayer.copy(slots = slots)
//    val updatedActingPlayer = actingPlayer.copy(
//        hand = actingPlayer.hand.filter { it.id != move.cardId }
//    )
//
//    val updatedPlayers = currentState.players.toMutableMap()
//
//    if (move.targetPlayerId == move.playerId) {
//        val mergedPlayer = updatedTargetPlayer.copy(hand = updatedActingPlayer.hand)
//        updatedPlayers[move.playerId] = mergedPlayer
//    } else {
//        updatedPlayers[move.targetPlayerId] = updatedTargetPlayer
//        updatedPlayers[move.playerId] = updatedActingPlayer
//    }
//
//    return currentState.copy(players = updatedPlayers)
//}
//
//private fun handleSwapDices(
//    currentState: GameState,
//    move: Move.SwapDices
//): GameState {
//    val player = currentState.players[move.playerId]?: return currentState
//    val slots = player.slots.toMutableList()
//
//    val first = slots[move.firstSlotIndex]
//    val second = slots[move.secondSlotIndex]
//
//    slots[move.firstSlotIndex] = first.copy(dice = second.dice)
//    slots[move.secondSlotIndex] = second.copy(dice = first.dice)
//
//    val updatedPlayer = player.copy(slots = slots)
//    return currentState.copy(players = currentState.players + (move.playerId to updatedPlayer))
//}
//
////private fun handleFinishGame(
////    currentState: GameState,
////    move: Move.FinishGame
////): GameState {
////    return currentState.copy(phase = GamePhase.FINISHED)
////}
//
//
//
//
