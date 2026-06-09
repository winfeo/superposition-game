package io.github.winfeo.superpositiongame.android.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.dto.rest.GameHistoryDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.UpdateUserDTO
import io.github.winfeo.superpositiongame.android.data.source.Network
import io.github.winfeo.superpositiongame.android.data.source.rest.AppModule
import io.github.winfeo.superpositiongame.android.data.source.rest.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = AppModule.gameHistoryRepository
    private val userRepository = AppModule.userRepository

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _recentGameHistory = MutableStateFlow<List<GameHistoryDTO>>(emptyList())
    val recentGameHistory: StateFlow<List<GameHistoryDTO>> = _recentGameHistory.asStateFlow()

    private val _isEditNicknameDialogVisible = MutableStateFlow(false)
    val isEditNicknameDialogVisible: StateFlow<Boolean> = _isEditNicknameDialogVisible.asStateFlow()

    private val _editNicknameError = MutableStateFlow<String?>(null)
    val editNicknameError: StateFlow<String?> = _editNicknameError.asStateFlow()


    init {
        viewModelScope.launch {
            UserSession.isAuthorized.combine(UserSession.currentUser) { isAuth, user ->
                ProfileState(isAuthorized = isAuth, user = user)
            }.collect { newState ->
                _state.value = newState

                if (newState.isAuthorized && newState.user != null) {
                    loadGameHistory(newState.user.id)
                    loadUserStats(newState.user.id)
                }
            }
        }
    }

    fun logout() {
        UserSession.logout()

        viewModelScope.launch {
            val guestIdResult = AppModule.guestRepository.createGuest()
            val guestId = guestIdResult.getOrNull()?: "guest-fallback-${System.currentTimeMillis()}"
            UserSession.setUserId(guestId)
            Network.disconnect()
            Network.connect(userId = guestId)
        }
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

    fun loadUserStats(userId: Long) {
        viewModelScope.launch {
            val result = userRepository.getUserStats(userId)
            result.onSuccess { updatedUser ->
                _state.value = _state.value.copy(user = updatedUser)
                UserSession.updateUser(updatedUser)
            }
        }
    }

    fun showEditNicknameDialog() {
        _editNicknameError.value = null
        _isEditNicknameDialogVisible.value = true
    }

    fun hideEditNicknameDialog() {
        _isEditNicknameDialogVisible.value = false
        _editNicknameError.value = null
    }

    fun updateNickname(newNickname: String) {
        val currentUser = _state.value.user ?: return
        viewModelScope.launch {
            val updateDTO = UpdateUserDTO(
                id = currentUser.id,
                nickname = newNickname,
                email = currentUser.email
            )
            val result = userRepository.updateUser(updateDTO)
            result.fold(
                onSuccess = { updatedUser ->
                    _state.value = _state.value.copy(user = updatedUser)
                    UserSession.updateUser(updatedUser)
                    hideEditNicknameDialog()
                },
                onFailure = { error ->
                    _editNicknameError.value = error.message
                }
            )
        }
    }
}
