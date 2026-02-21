package io.github.winfeo.superpositiongame.android.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//Вьюшка для экрана лобби
class LobbyViewModel: ViewModel() {
    private val _selectedPlayer = MutableStateFlow<String?>(null)
    val selectedPlayer: StateFlow<String?> = _selectedPlayer.asStateFlow()

    private val _playersList = MutableStateFlow<List<String>>(emptyList())
    val playersList: StateFlow<List<String>> = _playersList.asStateFlow()

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var refresh: Job? = null

    init {
        loadPlayersInLobby()
        startAutoRefresh()
    }

    fun loadPlayersInLobby(loading: Boolean = true) {
        viewModelScope.launch {
            _isLoading.value = loading
            try {
                // TODO загружать потом реальных игроков из фаербэйс, пока просто дилей
                delay(4000)
                _playersList.value = listOf(
                    "12345-67890",
                    "23456-78901",
                    "34567-89012"
                )
            } catch (e: Exception) {
                println("Отладка. Ошибка загрузки игроков из базы: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun startAutoRefresh() {
        refresh = viewModelScope.launch {
            while (true) {
                delay(10_000)
                loadPlayersInLobby(loading = false)
            }
        }
    }

    fun showInviteDialog(playerId: String) {
        _selectedPlayer.value = playerId
    }

    fun hideInviteDialog() {
        _selectedPlayer.value = null
    }

    fun sentInvite() {
        viewModelScope.launch {
            try {
                _selectedPlayer.value?.let { id ->
                    ///TODO добавить с файербэйсом взаимодействие
                    delay(1000)
                }
            }
            catch (e: Exception) {
                println("Отладка. Не удалось отправить сообщение игроку: ${_selectedPlayer.value}\nИсключение: $e")
            }
            finally {
                hideInviteDialog()
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        refresh?.cancel()
    }
}
