package io.github.winfeo.superpositiongame.android.ui.screen.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.repository.CardsRepositoryImpl
import io.github.winfeo.superpositiongame.android.domain.library.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(): ViewModel() {
    private val repository = CardsRepositoryImpl()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    init {
        loadAllCards()
    }

    private fun loadAllCards() {
        viewModelScope.launch {
            repository.getAllCards().collect { cards ->
                _cards.value = cards
            }
        }
    }
}
