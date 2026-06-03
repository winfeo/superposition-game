package io.github.winfeo.superpositiongame.android.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.dto.rest.GameHistoryDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.AppModule
import io.github.winfeo.superpositiongame.android.data.source.rest.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = AppModule.gameHistoryRepository

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _recentGameHistory = MutableStateFlow<List<GameHistoryDTO>>(emptyList())
    val recentGameHistory: StateFlow<List<GameHistoryDTO>> = _recentGameHistory.asStateFlow()

    init {
        viewModelScope.launch {
            UserSession.isAuthorized.combine(UserSession.currentUser) { isAuth, user ->
                ProfileState(isAuthorized = isAuth, user = user)
            }.collect { newState ->
                _state.value = newState

                if (newState.isAuthorized && newState.user != null) {
                    loadGameHistory(newState.user.id)
                }
            }
        }
    }

    fun logout() {
        UserSession.logout()
    }

    fun loadGameHistory(userId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoadingHistory = true,
                historyError = null
            )

            val result = repository.getGameHistory(userId)

            result.fold(
                onSuccess = { history ->
                    _state.value = _state.value.copy(
                        isLoadingHistory = false,
                        gameHistory = history
                    )

                    _recentGameHistory.value = history.take(5)
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoadingHistory = false,
                        historyError = error.message
                    )
                }
            )
        }
    }
}
