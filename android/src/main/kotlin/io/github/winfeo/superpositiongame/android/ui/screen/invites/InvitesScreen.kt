package io.github.winfeo.superpositiongame.android.ui.screen.invites

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invite

@Composable
fun InvitesScreen(
    viewModel: InvitesViewModel,
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
                state.invites.isEmpty() -> Text(stringResource(R.string.invites_emptyList))
                else -> InvitesList(
                    invites = state.invites
                )
            }
        }
    }
}

@Composable
fun InvitesList(
    invites: List<Invite>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(invites) { invite ->
            InviteCard(
                invite = invite
            )
        }
    }
}


@Composable
fun InviteCard(
    invite: Invite
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.invites_inviteText)}: ${invite.fromUserId.take(5)}",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}


@Preview(
    name = "Приглашения",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InvitesScreenPreview(){
    InvitesScreen(
        viewModel = viewModel(),
        onReturnToLobby = {})
}
