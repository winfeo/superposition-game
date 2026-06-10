package io.github.winfeo.superpositiongame.android.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.source.local.SettingsManager
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.domain.settings.AccountRepository
import io.github.winfeo.superpositiongame.android.domain.settings.usecase.DeleteAccountUseCase
import io.github.winfeo.superpositiongame.android.domain.settings.usecase.UpdateEmailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val repository: AccountRepository
) : ViewModel() {
    private val updateEmailUseCase = UpdateEmailUseCase(repository)
    private val deleteAccountUseCase = DeleteAccountUseCase(repository)


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
        val userId = UserSession.currentUser.value?.id?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = updateEmailUseCase(userId, newEmail)
            result.fold(
                onSuccess = { updatedUser ->
                    UserSession.updateUser(updatedUser)
                    _state.value = _state.value.copy(isLoading = false)
                    onSuccess()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                    onError(error.message?: "Ошибка смены почты")
                }
            )
        }
    }

    fun deleteAccount(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = UserSession.currentUser.value?.id?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = deleteAccountUseCase(userId)
            result.fold(
                onSuccess = {
                    UserSession.logout()
                    val guestIdResult = AppModule.guestRepository.createGuest()
                    val guestId = guestIdResult.getOrNull()?: "guest-fallback-${System.currentTimeMillis()}"
                    UserSession.setUserId(guestId)
                    Network.disconnect()
                    Network.connect(userId = guestId)
                    _state.value = _state.value.copy(isLoading = false)
                    onSuccess()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                    onError(error.message?: "Ошибка удаления аккаунта")
                }
            )
        }
    }
}
