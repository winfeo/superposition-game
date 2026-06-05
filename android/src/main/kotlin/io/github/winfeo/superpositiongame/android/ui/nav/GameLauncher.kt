package io.github.winfeo.superpositiongame.android.ui.nav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.repository.GameRepositoryImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

///TODO запуск игры если создана в базе?
class GameLauncher(): ViewModel() {
    private val repository = GameRepositoryImpl()
    private val _gameFlow = MutableStateFlow<String?>(null)
    val gameFlow: StateFlow<String?> = _gameFlow.asStateFlow()

    private var collectJob: Job? = null

    init {
        collectJob = viewModelScope.launch {
            repository.observeGameStart().collect { gameId ->
                _gameFlow.value = gameId
            }
        }
    }

    fun resetGameId() {
        _gameFlow.value = null
    }

    override fun onCleared() {
        super.onCleared()
        collectJob?.cancel()
    }
}

