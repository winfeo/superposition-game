package io.github.winfeo.superpositiongame.android.ui.nav

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.winfeo.superpositiongame.android.ui.screen.game.GameActivity
import io.github.winfeo.superpositiongame.android.ui.screen.invites.InvitesScreen
import io.github.winfeo.superpositiongame.android.ui.screen.invites.InvitationViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyScreen
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyViewModel

@Composable
fun Navigation(
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

    ///TODO временно потом DI
    val invitationViewModel: InvitationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InvitationViewModel(currentUserId) as T
            }
        }
    )

    ///TODO временно, подумать как переписать
    val context = LocalContext.current
    val viewModel: GameLauncher= viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameLauncher(
                    currentUserId
                ) as T
            }
        }
    )
    val gameId by viewModel.gameFlow.collectAsState()
    LaunchedEffect(gameId) {
        if (gameId != null) {
            context.startActivity(
                Intent(context, GameActivity::class.java)
                    .putExtra("GAME_ID", gameId)
                    .putExtra("USER_ID", currentUserId)
            )
        }
    }

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
                viewModel = invitationViewModel,
                onReturnToLobby = {
                    navController.popBackStack()
                }
            )
        }
    }
}
