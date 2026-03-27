package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun InviteDialog(
    playerId: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Предложение сыграть") },
        text = { Text(text = "Отправить предложение игроку ${playerId.take(5)}?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Отправить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Вернуться")
            }
        }
    )
}
