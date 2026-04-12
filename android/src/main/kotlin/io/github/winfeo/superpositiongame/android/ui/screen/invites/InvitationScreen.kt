package io.github.winfeo.superpositiongame.android.ui.screen.invites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

@Composable
fun InvitesScreen(
    viewModel: InvitationViewModel,
    onReturnToLobby: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.invites_title)) },
                navigationIcon = {
                    IconButton(onClick = onReturnToLobby) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back to lobby"
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
                state.invitations.isEmpty() -> Text(stringResource(R.string.invites_emptyList))
                else -> InvitesList(
                    invitations = state.invitations,
                    onAccept = { invitation ->
                        viewModel.acceptInvitation(invitation)
                    },
                    onReject = { invitation ->
                        viewModel.rejectInvitation(invitation)
                    }
                )
            }
        }
    }
}

@Composable
fun InvitesList(
    invitations: List<Invitation>,
    onAccept: (Invitation) -> Unit,
    onReject: (Invitation) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(invitations) { invite ->
            InviteCard(
                invitation = invite,
                onAccept = onAccept,
                onReject = onReject
            )
        }
    }
}


@Composable
fun InviteCard(
    invitation: Invitation,
    onAccept: (Invitation) -> Unit,
    onReject: (Invitation) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ///TODO добавить время отправки (поле уже есть в Invitations)
            Text(
                text = "${stringResource(R.string.invites_inviteText)}: ${invitation.senderId.take(5)}",
                style = MaterialTheme.typography.subtitle1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier.clickable {
                        onReject(invitation)
                    }
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        painter = painterResource(R.drawable.ic_reject),
                        contentDescription = "Reject invite",
                        tint = Color(red = 178, green = 34, blue = 34)
                    )
                }

                Box(
                    modifier = Modifier.clickable {
                        onAccept(invitation)
                    }
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        painter = painterResource(R.drawable.ic_accept),
                        contentDescription = "Accept invite",
                        tint = Color(red = 50, green = 205, blue = 50)
                    )
                }
            }
        }

    }
}


//@Preview(
//    name = "Приглашения",
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun InvitesScreenPreview(){
//    InvitesScreen(
//        viewModel = viewModel(),
//        onReturnToLobby = {})
//}

@Preview(
    name = "Список приглашений",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InvitesListPreview(){
    InvitesList(
        listOf(Invitation("111", "test11111111", sendTime = "10:23")),
        onAccept = {},
        onReject = {}
    )
}
