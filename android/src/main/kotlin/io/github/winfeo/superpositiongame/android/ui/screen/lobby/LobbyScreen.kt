package io.github.winfeo.superpositiongame.android.ui.screen.lobby

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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import io.github.winfeo.superpositiongame.android.ui.dialog.InviteDialog

///TODO добавить bottomBar для навигации по страницам
///TODO добавить тост или снекбар после отправки уведомления
///TODO если противник ответил положительно на приглашение, то показывать вверху
///убавляющуюся полоску с истечением времени (10 секунд) и возможностью отказаться от матча, потом запуск матча
//Лобби, лидерборд, библиотека карт, профиль (с настройками и статистикой?)

//экран лобби (отображаются игроки в сети, которые тоже находятся в лобби)
@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel,
    onInvitesClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val selectedPlayer by viewModel.selectedPlayer.collectAsState()

    Scaffold(
        topBar = {
            ///TODO иконка колокольчика в правом углу для просмота приглашений и ответа на них (принять или удалить) (отображение оклонённых приглашений)
            //Если есть новые приглшеня - отобрадение кружка на иконке колольчика
            TopAppBar(
                ///TODO отображение количества игроков в сети
                title = { Text(text = stringResource(R.string.lobby_title)) },
                actions = {
                    IconButton(onClick = onInvitesClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bell),
                            contentDescription = "invites"
                        )
                    }
                }
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
                state.isLoading -> CircularProgressIndicator()
                state.players.isEmpty() -> Text(text = stringResource(R.string.lobby_emptyList))
                else -> PlayersList(
                    players = state.players,
                    onPlayerClick = { player ->
                        viewModel.showInviteDialog(player)
                    }
                )
            }
        }
        selectedPlayer?.let { player ->
            InviteDialog(
                playerId = player.id,
                onConfirm = { viewModel.sentInvite() },
                onDismiss = { viewModel.hideInviteDialog() }
            )
        }
    }
}

@Composable
fun PlayersList(
    players: List<Player>,
    onPlayerClick: (Player) -> Unit
) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(players) { player ->
            PlayerCard(
                player = player,
                onClick = { onPlayerClick(player) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerCard(
    player: Player,
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
                text = "${stringResource(R.string.lobby_playerCardPlayer)}: ${player.id.take(5)}",
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
    LobbyScreen(
        viewModel = viewModel(),
        onInvitesClick = {}
    )
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

