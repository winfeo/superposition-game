package io.github.winfeo.superpositiongame.android.ui.dialog.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.card.description.CardDescription

@Composable
fun ReshuffleCardDialog(
    cards: List<Card>,
    maxSelectable: Int = 4,
    minSelectable: Int = 1,
    onCardsSelected: (List<Card>) -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }
    var selectedCards by remember { mutableStateOf<Set<String>>(emptySet()) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
//                    .fillMaxHeight(0.8f)
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "* выберите карты для сброса",
                        style = MaterialTheme.typography.body2,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                            .alpha(0.8f),
                    )

                    Text(
                        text = "${selectedCards.size} / $maxSelectable",
                        style = MaterialTheme.typography.body2,
                        color = Color.Blue,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cards, key = { it.id }) { card ->
                            CardItem(
                                card = card,
                                isSelected = selectedCards.contains(card.id),
                                onCardClick = {
                                    selectedCards = if (selectedCards.contains(card.id)) {
                                        selectedCards - card.id
                                    } else {
                                        if (selectedCards.size < maxSelectable) {
                                            selectedCards + card.id
                                        } else {
                                            selectedCards
                                        }
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (selectedCards.size in minSelectable..maxSelectable) {
                                val selected = cards.filter { it.id in selectedCards }
                                onCardsSelected(selected)
                            }
                        },
                        enabled = selectedCards.size in minSelectable..maxSelectable
                    ) {
                        Text("Подтвердить")
                    }
                }
            }
        }
    }
}

@Composable
private fun CardItem(
    card: Card,
    isSelected: Boolean,
    onCardClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        Color(0x4400FF00)
    } else {
        Color.Transparent
    }

    val borderColor = if (isSelected) {
        Color(0xFF00FF00)
    } else {
        Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onCardClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val cardImageId = getCardImageResource(card)
        Image(
            painter = painterResource(id = cardImageId),
            contentDescription = "Карта: ${card.type.name}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
        )

//        Spacer(modifier = Modifier.height(4.dp))
//
//        if (isSelected) {
//            Text(
//                text = "✓",
//                color = Color(0xFF00FF00),
//                style = MaterialTheme.typography.h6,
//                modifier = Modifier.padding(top = 4.dp)
//            )
//        }
    }
}

//TODO переделать!
fun getCardImageResource(card: Card): Int {
    return when (card.type) {
        CardType.PAULI -> getPauliResource(card)
        CardType.ROTATE -> getRotateResource(card)
        CardType.PHASE -> getPhaseResource(card)
        CardType.HADAMARD -> getHadamardResource(card)
        CardType.SWAP -> R.drawable.swap
        CardType.QUANTUM_NOISE -> R.drawable.quantum_noise
        CardType.KRONECKER_MULTIPLICATION -> R.drawable.kronecker_multiplication
        CardType.MEASUREMENT -> R.drawable.measurement
        CardType.IDENTITY -> R.drawable.identity
        CardType.BARRIER -> R.drawable.barrier
        CardType.RESHUFFLE -> R.drawable.reshuffle
//        CardType.QUANTUM_LUCKY -> R.drawable.quantum_lucky
    }
}

private fun getPauliResource(card: Card): Int {
    val axis = card.axis!!
    val radius = card.actionRadius

    return when (axis) {
        AxisRotation.X -> when (radius) {
            1 -> R.drawable.pauli_x
            else -> R.drawable.pauli_x3
        }
        AxisRotation.Y -> when (radius) {
            1 -> R.drawable.pauli_y
            else -> R.drawable.pauli_y3
        }
        AxisRotation.Z -> when (radius) {
            1 -> R.drawable.pauli_z
            else -> R.drawable.pauli_z3
        }
    }
}

private fun getRotateResource(card: Card): Int {
    val axis = card.axis!!

    return when (axis) {
        AxisRotation.X -> R.drawable.rotate_x
        AxisRotation.Y -> R.drawable.rotate_y
        AxisRotation.Z -> R.drawable.rotate_z
    }
}

private fun getPhaseResource(card: Card): Int {
    val isForward = card.isForwardRotation

    return when (isForward) {
        true -> R.drawable.phase_forward
        else -> R.drawable.phase_backward
    }
}

private fun getHadamardResource(card: Card): Int {
    val radius = card.actionRadius

    return when (radius) {
        1 -> R.drawable.hadamard
        else -> R.drawable.hadamard_3
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ReshuffleCardDialogPrev() {
    val testCards = listOf(
        Card(
            id = "1",
            textureId = null,
            description = object: CardDescription() {
                override val type = CardType.IDENTITY
                override val axis = null
                override val actionRadius = 0
                override val requiredSpecialSlot = false
                override val isForwardRotation = null
            }
        ),
        Card(
            id = "2",
            textureId = null,
            description = object : CardDescription() {
                override val type = CardType.SWAP
                override val axis = null
                override val actionRadius = 0
                override val requiredSpecialSlot = false
                override val isForwardRotation = null
            }
        ),
        Card(
            id = "3",
            textureId = null,
            description = object : CardDescription() {
                override val type = CardType.KRONECKER_MULTIPLICATION
                override val axis = null
                override val actionRadius = 0
                override val requiredSpecialSlot = false
                override val isForwardRotation = null
            }
        ),
        Card(
            id = "4",
            textureId = null,
            description = object : CardDescription() {
                override val type = CardType.KRONECKER_MULTIPLICATION
                override val axis = null
                override val actionRadius = 0
                override val requiredSpecialSlot = false
                override val isForwardRotation = null
            }
        )
    )

    MaterialTheme {
        ReshuffleCardDialog(
            cards = testCards,
            onCardsSelected = {}
        )
    }
}
