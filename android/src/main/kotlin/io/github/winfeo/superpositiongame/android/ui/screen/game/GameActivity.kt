package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.content.res.Resources
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import io.github.winfeo.superpositiongame.Main
import com.badlogic.gdx.Gdx
import io.github.winfeo.superpositiongame.model.game.GamePhase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

////TODO сделать на фрагментах пока?
class GameActivity: AndroidApplication() {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var game: Main
    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameId = intent.getStringExtra("GAME_ID")?:
        throw Resources.NotFoundException("Отладка. Игра не передана")
        val playerId = intent.getStringExtra("USER_ID")?:
        throw Resources.NotFoundException("Отладка. Не передан id игрока")

        viewModel = GameViewModel(
            gameId = gameId,
            playerId = playerId
        )

        game = Main (
            playerId = playerId,
            onMove = { move -> viewModel.sendMove(move) },
            getGameState = { viewModel.gameState.value!! }
        )
        initialize(game, AndroidApplicationConfiguration().apply {
            // Configure your application here.
            useImmersiveMode = true // Recommended, but not required.
        })

        observeGameState()
    }

    private fun observeGameState() {
        scope.launch {
            viewModel.gameState.collectLatest { state ->
                if (state == null) return@collectLatest

                if (state.phase == GamePhase.GAME_FINISHED) {
                    ///TODO выводить окно победы игрока
                }
                Gdx.app.postRunnable {
                    game.updateState(state)
                }
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
