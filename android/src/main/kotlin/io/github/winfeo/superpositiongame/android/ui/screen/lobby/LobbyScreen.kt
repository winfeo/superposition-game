package io.github.winfeo.superpositiongame.android.ui.screen.lobby

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.domain.lobby.model.User
import io.github.winfeo.superpositiongame.android.ui.dialog.InviteDialog
import io.github.winfeo.superpositiongame.android.ui.theme.elements.DiagonalCutShape

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
    val selectedPlayer by viewModel.selectedUser.collectAsState()

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
                state.users.isEmpty() -> Text(text = stringResource(R.string.lobby_emptyList))
                else -> UsersList(
                    users = state.users,
                    onUserClick = { user ->
                        viewModel.showInviteDialog(user)
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
fun UsersList(
    users: List<User>,
    onUserClick: (User) -> Unit
) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserCard(
                user = user,
                onClick = { onUserClick(user) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserCard(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
//            .background(Color.Red)
//            .padding(8.dp),
        elevation = 4.dp,
//        shape = RoundedCornerShape(16.dp),
        shape = DiagonalCutShape(),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_panda),
                    contentDescription = "Иконка игрока",
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .size(32.dp)
                )
                Text(
                    text = "${stringResource(R.string.lobby_playerCardPlayer)}: ${user.id.take(5)}",
                    style = MaterialTheme.typography.subtitle1
                )
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.15f)
                            ),
                            startX = 0f,
                            endX = Float.POSITIVE_INFINITY
                        )
                    )
            )
        }
    }
}


//@Preview(
//    name = "Лобби",
//    showSystemUi = true,
//    showBackground = true
//)
//@Composable
//fun LobbyScreenPreview() {
//    LobbyScreen(
//        viewModel = viewModel(),
//        onInvitesClick = {}
//    )
//}

@Preview(
    name = "Лобби",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun PlayerCardPreview() {
    UserCard(
        user = User(id = "12345-67890"),
        onClick = {}
    )
}

