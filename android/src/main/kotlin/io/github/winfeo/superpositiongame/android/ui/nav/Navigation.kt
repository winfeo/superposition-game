package io.github.winfeo.superpositiongame.android.ui.nav

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.winfeo.superpositiongame.android.ui.nav.route.AuthRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.InvitesRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.LibraryRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.LobbyRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.ProfileRoute
import io.github.winfeo.superpositiongame.android.ui.screen.game.GameActivity
import io.github.winfeo.superpositiongame.android.ui.screen.invites.InvitesScreen
import io.github.winfeo.superpositiongame.android.ui.screen.invites.InvitationViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.library.LibraryScreen
import io.github.winfeo.superpositiongame.android.ui.screen.library.LibraryViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyScreen
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.auth.AuthScreen
import io.github.winfeo.superpositiongame.android.ui.screen.auth.AuthViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.profile.ProfileScreen
import io.github.winfeo.superpositiongame.android.ui.screen.profile.ProfileViewModel

@Composable
fun Navigation(
    currentUserId: String
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    /* --------------- ViewModel-и --------------- */
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

    ///TODO временно потом DI
    val libraryViewModel: LibraryViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LibraryViewModel() as T
            }
        }
    )

    ///TODO временно потом DI
    val authViewModel: AuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel() as T
            }
        }
    )

    ///TODO временно потом DI
    val profileViewModel: ProfileViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel() as T
            }
        }
    )


    /* --------------- Запуск игры --------------- */
    ///TODO временно, подумать как переписать
    val context = LocalContext.current
    val viewModel: GameLauncher= viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameLauncher() as T
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


    /* --------------- Навигация --------------- */
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = LobbyRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<LobbyRoute> {
                LobbyScreen(
                    playerName = currentUserId,
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

            composable<LibraryRoute> {
                LibraryScreen(
                    viewModel = libraryViewModel
                )
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToAuth = {
                        navController.navigate(AuthRoute)
                    }
                )
            }

            composable<AuthRoute> {
                AuthScreen(
                    viewModel = authViewModel,
                    onSuccess = {
                        navController.popBackStack()
                    }
                )
            }
        }

        val showBottomBar = currentRoute in listOf(
            LobbyRoute::class.qualifiedName,
            LibraryRoute::class.qualifiedName,
            ProfileRoute::class.qualifiedName
        )

        if (showBottomBar) {
            BottomNavBar(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
