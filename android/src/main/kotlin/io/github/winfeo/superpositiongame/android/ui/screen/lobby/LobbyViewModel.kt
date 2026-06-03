package io.github.winfeo.superpositiongame.android.ui.screen.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.repository.LobbyRepositoryImpl
import io.github.winfeo.superpositiongame.android.domain.lobby.model.User
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.ObserveUsersUseCase
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.SendInvitationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

//Вьюшка для экрана лобби
class LobbyViewModel(
    private val currentUserId: String
): ViewModel() {
    private val repository = LobbyRepositoryImpl()
    private val observePlayers = ObserveUsersUseCase(repository, currentUserId)
    private val sendInvitation = SendInvitationUseCase(repository)

    private val _state = MutableStateFlow(LobbyState())
//    private val _state = MutableStateFlow(LobbyState.Loading) //TODO sealed?
    val state: StateFlow<LobbyState> = _state.asStateFlow()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()

    init {
        loadPlayersInLobby()
    }

    fun loadPlayersInLobby() {
        viewModelScope.launch {
            observePlayers()
                .onStart { _state.value = _state.value.copy(isLoading = true) }
                .catch { _state.value = _state.value.copy(isLoading = false, error = it.message) }
                .collect { users ->
                    _state.value = LobbyState(users = users, isLoading = false)
                }
        }
    }

    fun showInviteDialog(user: User) {
        _selectedUser.value = user
    }

    fun hideInviteDialog() {
        _selectedUser.value = null
    }

    fun sentInvite() { ///TODO сделать так, чтобы только один раз можно было отпрравить приглашение игроку (пока тот не отказался или не принял приглашение)
        val user = _selectedUser.value ?: return
        viewModelScope.launch {
            sendInvitation(currentUserId, user.id)
            hideInviteDialog()
        }
    }

}
