package io.github.winfeo.superpositiongame.android.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.source.rest.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            UserSession.isAuthorized.combine(UserSession.currentUser) { isAuth, user ->
                ProfileState(isAuthorized = isAuth, user = user)
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun logout() {
        UserSession.logout()
    }
}
