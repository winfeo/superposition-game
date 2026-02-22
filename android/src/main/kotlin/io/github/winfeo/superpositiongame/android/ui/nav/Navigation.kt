package io.github.winfeo.superpositiongame.android.ui.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.ObservePlayersUseCase
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.SendInvitationUseCase
import io.github.winfeo.superpositiongame.android.ui.screen.InvitesScreen
import io.github.winfeo.superpositiongame.android.ui.screen.LobbyScreen
import io.github.winfeo.superpositiongame.android.ui.viewModel.LobbyViewModel

@Composable
fun Navigation(
//    observePlayersUseCase: ObservePlayersUseCase,
//    sendInvitationUseCase: SendInvitationUseCase,
    currentUserId: String
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ///TODO временно потом DI
    val lobbyViewModel: LobbyViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LobbyViewModel(currentUserId) as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = LobbyRoute,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<LobbyRoute> {
            LobbyScreen(
                viewModel = lobbyViewModel,
                onInvitesClick = {
                    navController.navigate(InvitesRoute)
                }
            )
        }

        composable<InvitesRoute> {
            InvitesScreen(
                onReturnToLobby = {
                    navController.popBackStack()
                }
            )
        }
    }
}
