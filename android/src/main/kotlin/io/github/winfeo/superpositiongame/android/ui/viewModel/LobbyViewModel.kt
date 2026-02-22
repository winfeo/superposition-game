package io.github.winfeo.superpositiongame.android.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.winfeo.superpositiongame.android.data.repository.LobbyRepositoryImpl
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.ObservePlayersUseCase
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.SendInvitationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

//Вьюшка для экрана лобби
class LobbyViewModel(
//    private val observePlayers: ObservePlayersUseCase,
//    private val sendInvitation: SendInvitationUseCase,
    private val currentUserId: String
): ViewModel() {

    private val database = Firebase.database ///TODO переделать
    private val repository = LobbyRepositoryImpl(database)
    private val observePlayers = ObservePlayersUseCase(repository)
    private val sendInvitation = SendInvitationUseCase(repository)

    private val _state = MutableStateFlow(LobbyState())
    val state: StateFlow<LobbyState> = _state

    private val _selectedPlayer = MutableStateFlow<Player?>(null)
    val selectedPlayer: StateFlow<Player?> = _selectedPlayer.asStateFlow()

    init {
        loadPlayersInLobby()
    }

    fun loadPlayersInLobby() {
        viewModelScope.launch {
            observePlayers()
                .onStart { _state.value = _state.value.copy(isLoading = true) }
                .catch { _state.value = _state.value.copy(isLoading = false, error = it.message) }
                .collect { players ->
                    _state.value = LobbyState(players = players, isLoading = false)
                }
        }
    }

    fun showInviteDialog(player: Player) {
        _selectedPlayer.value = player
    }

    fun hideInviteDialog() {
        _selectedPlayer.value = null
    }

    fun sentInvite() {
        val player = _selectedPlayer.value ?: return
        viewModelScope.launch {
            sendInvitation(currentUserId, player.id)
            hideInviteDialog()
        }
    }

}
