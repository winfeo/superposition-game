package io.github.winfeo.superpositiongame.game

import io.github.winfeo.superpositiongame.ui.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Main : KtxGame<KtxScreen>() {

    override fun create() {
        KtxAsync.initiate()

        addScreen(MainScreen())
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

}

class MainScreen : KtxScreen {

    override fun render(delta: Float) {
    }

    override fun pause() {
        super.pause()

    }

    override fun resume() {
        super.resume()

    }

    override fun dispose() {

    }
}
