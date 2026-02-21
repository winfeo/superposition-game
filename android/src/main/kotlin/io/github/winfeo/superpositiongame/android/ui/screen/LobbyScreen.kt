package io.github.winfeo.superpositiongame.android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.ui.dialog.InviteDialog
import io.github.winfeo.superpositiongame.android.ui.viewModel.LobbyViewModel

///TODO добавить bottomBar для навигации по страницам
///TODO добавить тост или снекбар после отправки уведомления
//Лобби, лидерборд, профиль (с настройками?)

//экран лобби (отображаются игроки в сети, которые тоже находятся в лобби)
@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel
) {
    val playersList by viewModel.playersList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedPlayer by viewModel.selectedPlayer.collectAsState()


    Scaffold(
        topBar = {
            ///TODO обовлять список игроков каждые 10 секнуд в корутине?
            ///TODO иконка колокольчика в правом углу для просмота приглашений и ответа на них (принять или удалить) (отображение оклонённых приглашений)
            //Если есть новые приглшеня - отобрадение кружка на иконке колольчика
            TopAppBar(
                ///TODO отображение количества игроков в сети
                title = { Text(text = stringResource(R.string.lobby_title)) }
//                actions = {
//                    IconButton(onClick = {
//                        isLoading = true
//                    }) {
//                        Icon()
//                    }
//                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                playersList.isEmpty() -> Text(text = stringResource(R.string.lobby_emptyList))
                else -> PlayersList(
                    players = playersList,
                    onPlayerClick = { id ->
                        viewModel.showInviteDialog(id)
                    }
                )
            }
        }
        selectedPlayer?.let { id ->
            InviteDialog(
                playerId = id,
                onConfirm = { viewModel.sentInvite() },
                onDismiss = { viewModel.hideInviteDialog() }
            )
        }
    }
}

@Composable
fun PlayersList(
    players: List<String>,
    onPlayerClick: (String) -> Unit
) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(players) { player ->
            PlayerCard(
                playerId = player,
                onClick = { onPlayerClick(player) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerCard(
    playerId: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ///TODO заменить на ники в дальнейшем
            Text(
                text = "${stringResource(R.string.lobby_playerCardPlayer)}: ${playerId.take(5)}",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}


@Preview(
    name = "Лобби",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun LobbyScreenPreview() {
    LobbyScreen(viewModel = LobbyViewModel())
}

//@Preview(
//    name = "Лобби",
//    showSystemUi = true,
//    showBackground = true
//)
//@Composable
//fun PlayerCardPreview() {
//    PlayerCard(
//        playerId = "12345-67890",
//        onClick = {}
//    )
//}

