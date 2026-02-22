package io.github.winfeo.superpositiongame.android.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.winfeo.superpositiongame.R

@Composable
fun InvitesScreen(
    onReturnToLobby: () -> Unit
) {
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
                .padding(paddingValues)
        )
    }
}




@Preview(
    name = "Приглашения",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InvitesScreenPreview(){
    InvitesScreen(onReturnToLobby = {})
}
