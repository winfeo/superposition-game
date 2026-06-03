package io.github.winfeo.superpositiongame.android.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.dto.rest.NewUserDTO
import io.github.winfeo.superpositiongame.android.data.repository.AuthRepository
import io.github.winfeo.superpositiongame.android.data.source.rest.AppModule
import io.github.winfeo.superpositiongame.android.ui.screen.auth.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(): ViewModel() {
    private val repository = AppModule.authRepository

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email, error = null)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password, error = null)
    }

    fun login() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.value = currentState.copy(error = "Заполните все поля")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.login(NewUserDTO(currentState.email, currentState.password))
            result.fold(
                onSuccess = { _state.value = _state.value.copy(isLoading = false, isSuccess = true) },
                onFailure = { _state.value = _state.value.copy(isLoading = false, error = it.message) }
            )
        }
    }

    fun register() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.value = currentState.copy(error = "Заполните все поля")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.register(
                NewUserDTO(
                    currentState.email,
                    currentState.password
                )
            )
            result.fold(
                onSuccess = { _state.value = _state.value.copy(isLoading = false, isSuccess = true) },
                onFailure = { _state.value = _state.value.copy(isLoading = false, error = it.message) }
            )
        }
    }
}
