package io.github.winfeo.superpositiongame.android.data.repository

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.dto.InvitationDto
import io.github.winfeo.superpositiongame.android.data.dto.LobbyResponse
import io.github.winfeo.superpositiongame.android.data.source.Network
import io.github.winfeo.superpositiongame.android.data.toDomain
import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository
import io.github.winfeo.superpositiongame.android.domain.lobby.model.User
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class LobbyRepositoryImpl(): LobbyRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val topic = "/topic/lobby"
    private val initialData = "/app/lobby"

    override fun observeUsersInLobby(currentUserId: String): Flow<List<User>> {
        return callbackFlow {
            val connectionJob = launch {
                Network.connectionState.collect { isConnected ->
                    if (isConnected) {
                        Log.d("STOMP", "Подключение успешно")
                        Network.subscribeToTopic(topic) { message ->
                            handleMessage(
                                message = message,
                                userId = currentUserId
                            )
                        }

                        launch {
                            delay(500) ///TODO переделать
                            Network.sendMessage(initialData, "")
                        }
                    }
                }
            }

            awaitClose {
                connectionJob.cancel()
                Network.unsubscribeToTopic(topic)
                Network.unsubscribeToTopic(initialData)
            }
        }
    }

    private fun ProducerScope<List<User>>.handleMessage(
        message: String,
        userId: String
    ) {
        try {
            val dto = json.decodeFromString<LobbyResponse>(message)
            Log.d("STOMP", "Данные из ДТО: ${dto.users.joinToString { it.id }}"
            )
            val lobby = dto.toDomain()
            val users = lobby.users.filter { it.id != userId }
            trySend(users)
        } catch (e: Exception) {
            Log.d("LOBBY", "Ошибка парсинга: ${e.message}")
        }
    }

    override suspend fun sendInvitation(fromUserId: String, toUserId: String) {
        val dto = InvitationDto(fromUserId = fromUserId, toUserId = toUserId)
        val jsonString = json.encodeToString(InvitationDto.serializer(), dto)

        Network.sendMessage(
            destination = "/app/invite",
            message = jsonString
        )
    }

}
