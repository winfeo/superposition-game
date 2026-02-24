package io.github.winfeo.superpositiongame.android.ui.screen.invites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.winfeo.superpositiongame.android.data.repository.InvitationRepositoryImpl
import io.github.winfeo.superpositiongame.android.domain.invitations.usecase.AddListenerToInvitationUseCase
import io.github.winfeo.superpositiongame.android.domain.invitations.usecase.ObserveInvitationsUseCase
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class InvitesViewModel(
//    private val observeInvitations: ObserveInvitationsUseCase,
//    private val addListener: AddListenerToInvitationUseCase
    private val currentUserId: String
): ViewModel() {
    private val database = Firebase.database ///TODO переделать
    private val repository = InvitationRepositoryImpl(database)
    private val observeInvitations = ObserveInvitationsUseCase(repository)
    private val addListener = AddListenerToInvitationUseCase(repository)
    private val _state = MutableStateFlow(InvitesState())
    val state: StateFlow<InvitesState> = _state

    init {
        loadInvitations(currentUserId)
    }

    fun loadInvitations(userId: String) {
        viewModelScope.launch {
            observeInvitations(userId)
                .onStart { _state.value = _state.value.copy(isLoading = true) }
                .catch { _state.value = _state.value.copy(isLoading = false, error = it.message) }
                .collect { invites ->
                    _state.value = InvitesState(invites = invites, isLoading = false)
                }
        }
    }

    fun addListenerToInvitation(userId: String) {
        viewModelScope.launch {
            addListener(userId)
        }
    }
}
