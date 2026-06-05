package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSession {
    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    private val _currentUser = MutableStateFlow<AuthorisedUserDTO?>(null)
    val currentUser: StateFlow<AuthorisedUserDTO?> = _currentUser.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    fun login(user: AuthorisedUserDTO) {
        _currentUser.value = user
        _isAuthorized.value = true
    }

    fun logout() {
        _currentUser.value = null
        _isAuthorized.value = false
    }

    fun setUserId(userId: String) {
        _currentUserId.value = userId
    }

    fun updateUser(user: AuthorisedUserDTO) {
        _currentUser.value = user
    }

    fun clear() {
        _currentUserId.value = null
    }
}
