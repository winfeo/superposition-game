package io.github.winfeo.superpositiongame

import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.ui.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Main(
    private val playerId: String,
    private val onMove: (Move) -> Unit,
    private val getGameState: () -> GameState
): KtxGame<KtxScreen>() {
    private var opponentId = "" ///TODO удалить?

    override fun create() {
        KtxAsync.initiate()

        val screen = GameScreen(
            playerId = playerId,
            getOpponentId = { opponentId },
            onMove = onMove,
            getGameState = getGameState
        )
        addScreen(screen)
        setScreen<GameScreen>()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

    fun updateState(state: GameState) {
        val gameScreen = getScreen<GameScreen>()
        gameScreen.renderState(state)
    }

    ///TODO удалить потом
    fun updateOpponentId(newOpponentId: String) {
        this.opponentId = newOpponentId
    }

}
