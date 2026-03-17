package io.github.winfeo.superpositiongame.android.ui.nav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.winfeo.superpositiongame.android.data.repository.GameRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

///TODO запуск игры если создана в базе?
class GameLauncher(
    currentUserId: String
): ViewModel() {
    private val database = Firebase.database
    private val repository = GameRepositoryImpl(database)

    val gameFlow = repository
        .observeGameForUser(currentUserId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}

