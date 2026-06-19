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
import io.github.winfeo.superpositiongame.android.domain.invitations.model.InvitationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class InvitationRepositoryImpl: InvitationRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val topic = "/user/queue/invitations"
    private val acceptTopic = "/app/invite.accept"
    private val rejectTopic = "/app/invite.reject"
    private val initialData = "/app/invitations"

    private val _invitations = MutableStateFlow<List<Invitation>>(emptyList())

    private val _invitationEvents = MutableSharedFlow<InvitationEvent>(extraBufferCapacity = 10)
    val invitationEvents: SharedFlow<InvitationEvent> = _invitationEvents.asSharedFlow()

    private var globalListeningJob: Job? = null

    init {
        startGlobalListening()
    }

    private fun startGlobalListening() {
        globalListeningJob = CoroutineScope(Dispatchers.IO).launch {
            Network.connectionState.collect { isConnected ->
                if (isConnected) {
                    Log.d("STOMP", "Подключение успешно")
                    val currentInvitations = mutableListOf<Invitation>()

                    Network.subscribeToTopic(topic) { message ->
                        Log.d("INVITES", "Сообщение: $message")
                        try {
                            val event = json.decodeFromString<InvitationEventDTO>(message)
                            val eventType = InvitationEventType.valueOf(event.type)
                            when (eventType) {
                                InvitationEventType.INIT -> {
                                    currentInvitations.clear()
                                    event.invitations?.forEach {
                                        currentInvitations.add(it.toDomain())
                                    }
                                    _invitationEvents.tryEmit(
                                        InvitationEvent.Initialized(currentInvitations.toList())
                                    )
                                }
                                InvitationEventType.INVITE_SEND -> {
                                    event.invitation?.let {
                                        val domain = it.toDomain()
                                        currentInvitations.add(domain)
                                        _invitationEvents.tryEmit(InvitationEvent.New(domain))
                                    }
                                }
                                InvitationEventType.INVITE_REMOVED -> {
                                    event.invitation?.let { dto ->
                                        val domain = dto.toDomain()
                                        currentInvitations.removeIf {
                                            it.senderId == dto.senderId && it.sendTime == dto.sendTime
                                        }
                                        _invitationEvents.tryEmit(InvitationEvent.Removed(domain))
                                    }
                                }
                                InvitationEventType.INVITE_ACCEPTED -> {
                                    val copy = currentInvitations.toList()
                                    currentInvitations.clear()
                                    copy.forEach {
                                        _invitationEvents.tryEmit(InvitationEvent.Removed(it))
                                    }
                                }
                            }

                            _invitations.value = currentInvitations.toList()
                        } catch (e: Exception) {
                            Log.e("INVITES", "Ошибка: ${e.message}")
                        }
                    }

                    delay(500)
                    Network.sendMessage(initialData, "")
                    awaitCancellation()
                }
            }
        }
    }

    override fun observeInvitations(userId: String): Flow<List<Invitation>> = _invitations

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
