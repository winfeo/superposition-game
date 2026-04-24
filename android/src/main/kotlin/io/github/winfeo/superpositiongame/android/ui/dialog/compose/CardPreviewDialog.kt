package io.github.winfeo.superpositiongame.android.ui.dialog.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.CardDescription

@Composable
fun CardPreviewDialog(
    card: Card,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            backgroundColor = Color(0xFF37373C)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val cardImageId = getCardImageResource(card)
                Image(
                    painter = painterResource(id = cardImageId),
                    contentDescription = "Карта: ${card.type.name}",
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(0.7f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, Color.LightGray, RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CardPreviewDialogPrev() {
    val card = Card(
        id = "1",
        textureId = null,
        description = object : CardDescription() {
            override val type = CardType.KRONECKER_MULTIPLICATION
            override val axis = null
            override val actionRadius = 0
            override val requiredSpecialSlot = false
            override val isForwardRotation = null
        }
    )
    CardPreviewDialog(
        card = card,
        onDismiss = {}
    )
}
