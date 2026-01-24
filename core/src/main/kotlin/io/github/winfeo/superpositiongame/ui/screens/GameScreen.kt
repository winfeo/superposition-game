package io.github.winfeo.superpositiongame.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.winfeo.superpositiongame.configs.GameConfig
import io.github.winfeo.superpositiongame.managers.CardsAtlasManager
import io.github.winfeo.superpositiongame.managers.DiceAtlasManager
import io.github.winfeo.superpositiongame.ui.GameTableCard
import io.github.winfeo.superpositiongame.ui.GameTableDice
import ktx.app.KtxScreen

// Класс для отрисовки игрового поля
class GameScreen : KtxScreen {
    private val stage = Stage(ScreenViewport())
    private lateinit var gameTableCard: GameTableCard
    private lateinit var gameTableDice: GameTableDice
    /// TODO реализовать прокурчивающийся полукругом список карт для выбора игрока
    ///TODO переписать на паттерны ECS и FSM? Игровые паттерны

    init {
        CardsAtlasManager.loadAtlas()
        DiceAtlasManager.loadAtlas()
    }

    override fun show() {
        super.show()

        Gdx.input.inputProcessor = stage

        GameConfig.init(
            //screenWidth = stage.width.also { println("screenWidth = $it") },
            //screenHeight = stage.height.also { println("screenHeight = $it") })
            screenWidth = stage.viewport.worldWidth.also { println("screenWidth = $it") },
            screenHeight = stage.viewport.worldHeight.also { println("screenHeight = $it") })

        gameTableCard = GameTableCard()
        stage.addActor(gameTableCard)
        gameTableDice = GameTableDice(gameTableCard)
        stage.addActor(gameTableDice)
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

        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        super.dispose()

        stage.dispose()
        CardsAtlasManager.dispose()
        DiceAtlasManager.dispose()
    }
}
