package io.github.winfeo.superpositiongame.android.ui.screen.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.domain.game.usecase.ObserveGameStartUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameLauncher(
    private val repository: GameRepository
): ViewModel() {
    private val observeGameStartUseCase = ObserveGameStartUseCase(repository)
    private val _gameFlow = MutableStateFlow<String?>(null)
    val gameFlow: StateFlow<String?> = _gameFlow.asStateFlow()

    private var collectJob: Job? = null

    init {
        collectJob = viewModelScope.launch {
            observeGameStartUseCase().collect { gameId ->
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
