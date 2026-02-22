package io.github.winfeo.superpositiongame.android.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation
import io.github.winfeo.superpositiongame.android.domain.invitations.usecase.ObserveInvitationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InvitesViewModel(
    private val observeInvitations: ObserveInvitationsUseCase
): ViewModel() {
    private val _state = MutableStateFlow<List<Invitation>>(emptyList())
    val state: StateFlow<List<Invitation>> = _state

    fun loadInvitations(userId: String) {
        viewModelScope.launch {
            observeInvitations(userId).collect { _state.value = it }
        }
    }
}
