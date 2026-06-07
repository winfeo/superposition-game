package io.github.winfeo.superpositiongame.android.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.source.Network
import io.github.winfeo.superpositiongame.android.data.source.rest.AppModule
import io.github.winfeo.superpositiongame.android.data.source.rest.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsManager.isMusicEnabled.collect { music ->
                _state.value = _state.value.copy(isMusicEnabled = music)
            }
        }
        viewModelScope.launch {
            settingsManager.isInviteSoundEnabled.collect { sound ->
                _state.value = _state.value.copy(isInviteSoundEnabled = sound)
            }
        }
    }

    fun toggleMusic(enabled: Boolean) {
        settingsManager.setMusicEnabled(enabled)
    }

    fun toggleInviteSound(enabled: Boolean) {
        settingsManager.setInviteSoundEnabled(enabled)
    }

    fun changeEmail(
        newEmail: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = UserSession.currentUser.value?: return
        viewModelScope.launch {
            val result = AppModule.userRepository.updateEmail(currentUser.id, newEmail)
            result.fold(
                onSuccess = { updatedUser ->
                    UserSession.updateUser(updatedUser)
                    onSuccess()
                },
                onFailure = { error ->
                    onError(error.message?: "Ошибка смены почты")
                }
            )
        }
    }

    fun deleteAccount(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = UserSession.currentUser.value ?: return
        viewModelScope.launch {
            val result = AppModule.userRepository.deleteAccount(currentUser.id)
            result.fold(
                onSuccess = {
                    UserSession.logout()
                    val guestIdResult = AppModule.guestRepository.createGuest()
                    val guestId = guestIdResult.getOrNull()?: "guest-fallback-${System.currentTimeMillis()}"
                    UserSession.setUserId(guestId)
                    Network.disconnect()
                    Network.connect(userId = guestId)
                    onSuccess()
                },
                onFailure = { error ->
                    onError(error.message?: "Ошибка удаления аккаунта")
                }
            )
        }
    }
}
