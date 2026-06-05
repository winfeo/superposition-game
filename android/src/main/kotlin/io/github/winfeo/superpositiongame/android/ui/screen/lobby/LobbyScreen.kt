package io.github.winfeo.superpositiongame.android.ui.screen.lobby

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.source.rest.UserSession
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import io.github.winfeo.superpositiongame.android.ui.dialog.InviteDialog
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur

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
    val user by UserSession.currentUser.collectAsState()
    val userId by UserSession.currentUserId.collectAsState()

    Scaffold { paddingValues ->
        Box(
            Modifier.background(Color(0xFF0C0813))
        ) {
            LobbyBackground()

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val playerName = user?.nickname?: userId?: ""
                UserBar(
                    playerName = playerName.take(9),
                    onInvitesClick = onInvitesClick
                )

                HeaderDivider()

                Box {
                    when {
                        state.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = Color(0xFF6C8CFF)
                                )
                            }
                        }
                        state.players.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.lobby_emptyList),
                                    color = Color.White.copy(alpha = 0.8f),
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                        else -> UsersList(
                            players = state.players,
                            onUserClick = { user ->
                                viewModel.showInviteDialog(user)
                            }
                        )
                    }
                }
            }
        }
    }

    selectedPlayer?.let { player ->
        InviteDialog(
            playerName = player.nickname?: player.id,
            onConfirm = { viewModel.sendInvite() },
            onDismiss = { viewModel.hideInviteDialog() }
        )
    }
}

@Composable
fun LobbyBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundBlur()

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Color.White.copy(alpha = 0.02f)
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.35f)
                        ),
                        radius = 1400f
                    )
                )
        )
    }
}

@Composable
fun UserBar(
    playerName: String,
    onInvitesClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2B36A6).copy(alpha = 0.45f),
                        Color(0xFF15162A).copy(alpha = 0.35f)
                    )
                )
            )
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6C8CFF).copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        radius = 900f
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AvatarWithName(playerName)

            Spacer(modifier = Modifier.weight(1f))

            NotificationButton(onInvitesClick)
        }
    }
}

@Composable
fun AvatarWithName(
    playerName: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                color = Color.White.copy(alpha = 0.10f),
                shape = RoundedCornerShape(16.dp),
                width = 1.dp
                )
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Color.White.copy(alpha = 0.10f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_panda),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = playerName,
            color = Color.White.copy(alpha = 0.92f)
        )
    }
}

@Composable
fun NotificationButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.06f)
                    )
                ),
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.12f),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Icon(
            painter = painterResource(R.drawable.ic_bell),
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.92f),
            modifier = Modifier.size(24.dp)
        )

//        Box( //TODO рисовать, когда есть уведомления
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .size(8.dp)
//                .background(
//                    color = Color(0xFF3D4AEB),
//                    shape = CircleShape
//                )
//                .border(
//                    width = 1.dp,
//                    color = Color.White.copy(alpha = 0.8f),
//                    shape = CircleShape
//                )
//        )

    }
}

@Composable
fun HeaderDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.12f),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
fun UsersList(
    players: List<Player>,
    onUserClick: (Player) -> Unit
) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(players) { user ->
            val playerName = user.nickname?: user.id
            UserCard(
                playerName = playerName.take(9),
                onClick = { onUserClick(user) }
            )
        }
    }
}

@Composable
fun UserCard(
    playerName: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clickable { onClick() }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.06f),
                        Color.White.copy(alpha = 0.03f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
//                shape = DiagonalCutShape() //TODO закастомить тоже?
            )
            .border(
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp),
                width = 1.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.08f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_panda),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.lobby_playerCardPlayer),
                    color = Color.White.copy(alpha = 0.5f)
                )
                Text(
                    playerName,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(6.dp)
                    .background(
                        color = Color(0xFF6CFF8F),
                        shape = CircleShape
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

//@Preview(
//    name = "Лобби",
//    showSystemUi = true,
//    showBackground = true
//)
//@Composable
//fun PlayerCardPreview() {
//    UserCard(
//        user = User(id = "12345-67890"),
//        onClick = {}
//    )
//}

//@Preview(
//    name = "Лобби",
//    showSystemUi = true,
//    showBackground = true
//)
//@Composable
//fun UserBarPrev() {
//    UserBar(
//        playerName = "12345",
//        onInvitesClick = {}
//    )
//}


@Composable
fun LobbyScreenContent() {
    Box(
        Modifier.background(Color(0xFF0C0813))
    ) {
        LobbyBackground()

        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            UserBar(
                playerName = "guest-1111111111",
                onInvitesClick = {}
            )

            HeaderDivider()

            Box {
                UsersList(
                    players = listOf(
                        Player(
                            id = "1234567890",
                            nickname = "Winfeo"
                        )
                    ),
                    onUserClick = {}
                )
            }
        }
    }
}

@Preview(
    name = "Лобби",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun LobbyScreenPrev() {
    LobbyScreenContent()
}

