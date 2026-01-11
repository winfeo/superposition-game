package io.github.winfeo.superpositiongame.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.configs.GameConfig
import io.github.winfeo.superpositiongame.managers.CardsAtlasManager
import io.github.winfeo.superpositiongame.ui.GameTable
import io.github.winfeo.superpositiongame.utils.GraphicsUtils
import ktx.app.KtxScreen

// Класс для отрисовки игрового поля
class GameScreen : KtxScreen {
    private val stage = Stage()
    private lateinit var gameTable: GameTable
    /// TODO реализовать прокурчивающийся полукругом список карт для выбора игрока

    init {
        CardsAtlasManager.loadAtlas()
    }

    override fun show() {
        super.show()

        GameConfig.init(
            screenWidth = stage.width.also { println("screenWidth = $it") },
            screenHeight = stage.height.also { println("screenHeight = $it") })

        GraphicsUtils.initShapeDrawer(stage.batch as SpriteBatch)

        gameTable = GameTable()
        stage.addActor(gameTable)
        //stage.isDebugAll = true


    }

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        stage.viewport.update(width, height)
    }

    override fun dispose() {
        super.dispose()

        stage.dispose()
        CardsAtlasManager.dispose()
    }
}
