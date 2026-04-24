package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import io.github.winfeo.superpositiongame.Main
import io.github.winfeo.superpositiongame.android.ui.dialog.GameDialogState
import io.github.winfeo.superpositiongame.android.ui.dialog.GameDialogs
import io.github.winfeo.superpositiongame.android.ui.dialog.compose.ReshuffleCardDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.compose.RotateCardDialog
import io.github.winfeo.superpositiongame.android.ui.theme.SuperpositionGameTheme
import io.github.winfeo.superpositiongame.model.game.GamePhase

class GameActivity: AppCompatActivity(), AndroidFragmentApplication.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameId = intent.getStringExtra("GAME_ID")
            ?: throw Resources.NotFoundException("Отладка. Игра не передана")
        val playerId = intent.getStringExtra("USER_ID")
            ?: throw Resources.NotFoundException("Отладка. Не передан id игрока")

        val viewModel = GameViewModel(
            gameId = gameId,
            playerId = playerId
        )
        val dialogs = GameDialogs(viewModel)
        val game = Main(
            playerId = playerId,
            dialogs = dialogs,
            onMove = { viewModel.sendMove(it) },
            getGameState = { viewModel.gameState.value!! }
        )

        if (savedInstanceState == null) {
            val fragment = GameFragment().apply { this.game = game }
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit()
        }

        setContent {
            SuperpositionGameTheme {
                val gameState by viewModel.gameState.collectAsState()
                val dialogState by viewModel.dialogState.collectAsState()

                LaunchedEffect(gameState) {
                    val state = gameState?: return@LaunchedEffect

                    if (state.phase == GamePhase.GAME_FINISHED) {
                        // TODO: показать диалог победы
                    }

//                    Gdx.app.postRunnable {
//                        game.updateState(state)
//                    }
                    game.applyNewState(state)
                }

                //TODO сделать отдельный stage в GameScreen для диалогов (блокировать экран игры при показе диалога)
                Box(modifier = Modifier.fillMaxSize()) {
                    dialogState?.let { dialog ->
                        when (dialog) {
                            is GameDialogState.RotateDialog -> {
                                RotateCardDialog(
                                    availableStates = dialog.availableStates,
                                    onStateSelected = { selected ->
                                        dialog.onStateSelected(selected)
                                        viewModel.dismissDialog()
                                    }
                                )
                            }
                            is GameDialogState.ReshuffleDialog -> {
                                ReshuffleCardDialog(
                                    cards = dialog.cards,
                                    maxSelectable = dialog.maxSelectable,
                                    minSelectable = dialog.minSelectable,
                                    onCardsSelected = { selectedCards ->
                                        dialog.onCardsSelected(selectedCards)
                                        viewModel.dismissDialog()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun exit() {
        finish()
    }
}
