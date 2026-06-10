package io.github.winfeo.superpositiongame.android.data.repository

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.dto.socket.InvitationDTO
import io.github.winfeo.superpositiongame.android.data.dto.socket.InvitationEventDTO
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository
import io.github.winfeo.superpositiongame.android.data.dto.socket.InvitationEventType
import io.github.winfeo.superpositiongame.android.data.util.toDto
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class InvitationRepositoryImpl: InvitationRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val topic = "/user/queue/invitations"
    private val acceptTopic = "/app/invite.accept"
    private val rejectTopic = "/app/invite.reject"
    private val initialData = "/app/invitations"

    override fun observeInvitations(userId: String): Flow<List<Invitation>> {
        return callbackFlow {
            val connectionJob = launch {
                Network.connectionState.collect { isConnected ->
                    if (isConnected) {
                        Log.d("STOMP", "Подключение успешно")

                        val currentInvitations = mutableListOf<Invitation>()
                        Network.subscribeToTopic(topic) { message ->
                            Log.d("INVITE", "Message: $message")
                            try {
                                val event = json.decodeFromString<InvitationEventDTO>(message)
                                val invitationEventType = InvitationEventType.valueOf(event.type)
                                Log.d("INVITE", "Event type: ${event.type}")
                                when(invitationEventType){
                                    InvitationEventType.INIT -> {
                                        currentInvitations.clear()
                                        event.invitations?.forEach {
                                            currentInvitations.add(it.toDomain())
                                        }
                                    }

                                    InvitationEventType.INVITE_SEND -> {
                                        event.invitation?.let {
                                            currentInvitations.add(it.toDomain())
                                        }
                                    }

                                    InvitationEventType.INVITE_REMOVED -> {
                                        event.invitation?.let { dto ->
                                            currentInvitations.removeIf {
                                                it.senderId == dto.senderId
                                            }
                                        }
                                    }

                                    InvitationEventType.INVITE_ACCEPTED -> {
                                        currentInvitations.clear()
                                    }
                                }

                                trySend(currentInvitations.toList())
                            } catch (e: Exception) {
                                Log.d("INVITES", "Ошибка: ${e.message}")
                            }
                        }

                        launch {
                            delay(500)
                            Network.sendMessage(initialData, "")
                        }
                    }
                }
            }

            awaitClose {
                connectionJob.cancel()
                Network.unsubscribeToTopic(topic)
            }
        }

    }

    override suspend fun acceptInvitation(
        invitation: Invitation,
        currentUserId: String
    ) {
        val dto = invitation.toDto(currentUserId)
        val payload = json.encodeToString(InvitationDTO.serializer(), dto)
        Network.sendMessage(
            destination = acceptTopic,
            message = payload
        )
    }

    override suspend fun rejectInvitation(
        invitation: Invitation,
        currentUserId: String
    ) {
        val dto = invitation.toDto(currentUserId)
        val payload = json.encodeToString(InvitationDTO.serializer(), dto)
        Network.sendMessage(
            destination = rejectTopic,
            message = payload
        )
    }

}
