package io.github.winfeo.superpositiongame.android.ui.dialog.compose

import io.github.winfeo.superpositiongame.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.model.dice.DiceState

@Composable
fun RotateCardDialog(
    availableStates: List<DiceState>,
    onStateSelected: (DiceState) -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    Text(
//                        text = "Выберите состояние кубита",
//                        style = MaterialTheme.typography.h6,
//                        modifier = Modifier.padding(bottom = 16.dp)
//                    )

                    DiceStatesGrid(
                        availableStates = availableStates,
                        onStateClick = { selectedState ->
                            showDialog = false
                            onStateSelected(selectedState)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiceStatesGrid(
    availableStates: List<DiceState>,
    onStateClick: (DiceState) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        availableStates.chunked(3).forEach { rowStates ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowStates.forEach { state ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { onStateClick(state) }
                            .border(
                                2.dp,
                                Color.DarkGray,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val stateId = when (state) {
                            DiceState.ZERO -> R.drawable.zero
                            DiceState.ONE -> R.drawable.one
                            DiceState.PLUS -> R.drawable.plus
                            DiceState.MINUS -> R.drawable.minus
                            DiceState.I -> R.drawable.i_plus
                            DiceState.I_MINUS -> R.drawable.i_minus
                        }

                        Image(
                            painter = painterResource(id = stateId),
                            contentDescription = "Состояние: ${state.name}",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RotateCardDialogPrev() {
    RotateCardDialog(
        availableStates = listOf(DiceState.MINUS, DiceState.ZERO, DiceState.PLUS),
        onStateSelected = {}
    )
}
