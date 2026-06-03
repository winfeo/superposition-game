package io.github.winfeo.superpositiongame.android.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToAuth: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    if (state.isAuthorized && state.user != null) {
        AuthorizedProfileScreen(user = state.user!!)
    } else {
        UnauthorizedProfileScreen(
            onLoginClick = onNavigateToAuth
        )
    }
}
