package io.github.winfeo.superpositiongame

import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.ui.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Main(
    private val playerId: String,
    private val dialogs: Dialogs,
    private val onMove: (Move) -> Unit,
    private val getGameState: () -> GameState
): KtxGame<KtxScreen>() {
    @Volatile
    private var pendingState: GameState? = null

    private var gameScreen: GameScreen? = null
    private var opponentId = "" ///TODO удалить?

    override fun create() {
        KtxAsync.initiate()

        val screen = GameScreen(
            playerId = playerId,
            getOpponentId = { opponentId },
            dialogs = dialogs,
            onMove = onMove,
            getGameState = getGameState,
            applyPendingState = { updateState() }
        )
        gameScreen = screen
        addScreen(screen)
        setScreen<GameScreen>()
    }

    fun applyNewState(state: GameState) {
        pendingState = state
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

//    fun updateState(state: GameState) {
//        val gameScreen = getScreen<GameScreen>()
//        gameScreen.renderState(state)
//    }

    fun updateState() {
        pendingState?.let { state ->
            gameScreen?.renderState(state)
            pendingState = null
        }
    }

    ///TODO удалить потом
    fun updateOpponentId(newOpponentId: String) {
        this.opponentId = newOpponentId
    }

}
