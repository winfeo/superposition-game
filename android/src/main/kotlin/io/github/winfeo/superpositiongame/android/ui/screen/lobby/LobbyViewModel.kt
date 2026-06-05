package io.github.winfeo.superpositiongame.android.ui.screen.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.repository.LobbyRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.source.rest.UserSession
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.ObservePlayersUseCase
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.SendInvitationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

//Вьюшка для экрана лобби
class LobbyViewModel(): ViewModel() {
    private val repository = LobbyRepositoryImpl()
    private val sendInvitation = SendInvitationUseCase(repository)

    private val _state = MutableStateFlow(LobbyState())
//    private val _state = MutableStateFlow(LobbyState.Loading) //TODO sealed?
    val state: StateFlow<LobbyState> = _state.asStateFlow()

    private val _selectedPlayer = MutableStateFlow<Player?>(null)
    val selectedPlayer: StateFlow<Player?> = _selectedPlayer.asStateFlow()
    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            UserSession.currentUserId.collect { userId ->
                if (userId != null) {
                    loadPlayersInLobby(userId)
                }
            }
        }
    }

    fun loadPlayersInLobby(currentUserId: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val observePlayers = ObservePlayersUseCase(repository, currentUserId)
            observePlayers()
                .onStart { _state.value = _state.value.copy(isLoading = true) }
                .catch { _state.value = _state.value.copy(isLoading = false, error = it.message) }
                .collect { users ->
                    _state.value = LobbyState(players = users, isLoading = false)
                }
        }
    }

    fun showInviteDialog(player: Player) {
        _selectedPlayer.value = player
    }

    fun hideInviteDialog() {
        _selectedPlayer.value = null
    }

    fun sendInvite() {
        val receiverUser = _selectedPlayer.value?: return
        val receiverId = receiverUser.id
        val senderId = UserSession.currentUserId.value?: return
        val senderNickname = UserSession.currentUser.value?.nickname

        viewModelScope.launch {
            sendInvitation(
                senderId = senderId,
                senderNickname = senderNickname,
                receiverId = receiverId
            )
            hideInviteDialog()
        }
    }

}
