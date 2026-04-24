package io.github.winfeo.superpositiongame.android.data.repository

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.dto.move.DoubleTapEffectDto
import io.github.winfeo.superpositiongame.android.data.dto.move.MoveDto
import io.github.winfeo.superpositiongame.android.data.dto.move.PlayCardDto
import io.github.winfeo.superpositiongame.android.data.dto.move.ReshuffleCardDto
import io.github.winfeo.superpositiongame.android.data.dto.move.RotateDiceDto
import io.github.winfeo.superpositiongame.android.data.dto.move.SwapDicesDto
import io.github.winfeo.superpositiongame.android.data.dto.state.GameStateDto
import io.github.winfeo.superpositiongame.android.data.source.Network
import io.github.winfeo.superpositiongame.android.data.toDomain
import io.github.winfeo.superpositiongame.android.data.toDto
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.ui.screen.game.GameStartEvent
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class GameRepositoryImpl(): GameRepository {
    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
        serializersModule = SerializersModule {
            polymorphic(MoveDto::class) {
                subclass(PlayCardDto::class)
                subclass(RotateDiceDto::class)
                subclass(SwapDicesDto::class)
                subclass(DoubleTapEffectDto::class)
                subclass(ReshuffleCardDto::class)
            }
        }
    }
    val topic = "/user/queue/game"
    val sendTopic = "/app/game/"
    val gameStart = "/user/queue/game.start"

    override suspend fun sendMove(
        gameId: String,
        move: Move
    ) {
        val dto = move.toDto()
        val payload = json.encodeToString(MoveDto.serializer(), dto)
        Log.d("GAME_SEND", payload)
        Network.sendMessage(
            destination = "$sendTopic/$gameId/move",
            message = payload
        )
    }

    override fun observeGameState(gameId: String, playerId: String): Flow<GameState> {
        return callbackFlow {
            ///TODO добавить сначала проверку, что подключились. Или отправлять на сервер сообщение о готовности
            launch {
                Log.d("GAME_SET", "Работа метода")
                Network.subscribeToTopic(topic) { message ->
                    try {
                        val stateDto = json.decodeFromString<GameStateDto>(message)
                        Log.d("GAME_STATE", """
                            Получено состояние:
                            ${stateDto.phase}
                            ${stateDto.currentPlayerId}
                            ${stateDto.turnNumber}
                            players:
                                ${stateDto.players.entries.joinToString("\n") { (id, player) ->
                                    "Player ${id.take(5)} | hand=${player.hand.size} | slots=${player.slots.size}" +
                                        "DiceState=${player.slots.joinToString { it.initialDice.state }}" +
                                        "SlotOwner=${player.slots.joinToString { it.ownerId }}"
                                }}
                        """.trimIndent())
                        val gameState = stateDto.toDomain(playerId)
                        trySend(gameState)
                    } catch (e: Exception) {
                        Log.d("GAME_STATE", "Ошибка получения состояния: ${e.message}")
                    }

                }
            }


            awaitClose {
                Network.unsubscribeToTopic(topic)
            }
        }
    }

    override fun observeGameStart(): Flow<String> {
        return callbackFlow {
            Network.subscribeToTopic(gameStart) { message ->
                try {
                    val event = json.decodeFromString<GameStartEvent>(message)
                    trySend(event.gameId)
                    Log.d("GAME_START", "Получен gameId: ${event.gameId}")
                } catch (e: Exception) {
                    Log.d("GAME_START", "Ошибка получения gameId: ${e.message}")
                }
            }

            ///TODO отписывать после успешного получения сразу?
            awaitClose {
                Network.unsubscribeToTopic(gameStart)
            }
        }
    }

}
