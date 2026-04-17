package io.github.winfeo.superpositiongame.android.ui.nav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.repository.GameRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

///TODO запуск игры если создана в базе?
class GameLauncher(): ViewModel() {
    private val repository = GameRepositoryImpl()
    val gameFlow = repository.observeGameStart()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}

