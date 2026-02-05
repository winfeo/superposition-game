package io.github.winfeo.superpositiongame.ui.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.game.GameCycle
import io.github.winfeo.superpositiongame.graphics.GameSkinFactory
import io.github.winfeo.superpositiongame.manager.CardsAtlasManager
import io.github.winfeo.superpositiongame.manager.DiceAtlasManager
import io.github.winfeo.superpositiongame.ui.GameTable
import io.github.winfeo.superpositiongame.graphics.RotateSelectionDialog
import io.github.winfeo.superpositiongame.graphics.VictoryDialog
import io.github.winfeo.superpositiongame.manager.PlayerHandManager
import io.github.winfeo.superpositiongame.manager.doubleTap.GameTapController
import io.github.winfeo.superpositiongame.manager.dragAndDrop.GameDragController
import io.github.winfeo.superpositiongame.ui.GameTimer
import ktx.app.KtxScreen

// Класс для отрисовки игрового поля
class GameScreen : KtxScreen {
    private val stage = Stage(ScreenViewport())
    private lateinit var gameTable: GameTable
    private lateinit var timerLabel: Label
    private lateinit var gameTimer: GameTimer
    private lateinit var playerHand: PlayerHandManager
    /// TODO реализовать прокурчивающийся полукругом список карт для выбора игрока
    ///TODO переписать на паттерны ECS и FSM? Игровые паттерны

    init {
        CardsAtlasManager.loadAtlas()
        DiceAtlasManager.loadAtlas()
    }

    override fun show() {
        super.show()

        Gdx.input.inputProcessor = stage

        GameConfig.init(stage = stage)

        val dragController = GameDragController()
        val tapController = GameTapController()

        playerHand = PlayerHandManager(
            stage = stage,
            dragController = dragController,
            tapController = tapController
        )
        dragController.init(playerHand)
        tapController.init(playerHand)

        gameTable = GameTable(dragController, tapController)
        stage.addActor(gameTable)
        ///TODO перенсти создание диалогового окна? Создать статический скин?
        RotateSelectionDialog.init(stage)
        //CardCircleLayout.init(stage)

        createTimer()
        GameCycle.setGameTimer(gameTimer)
        GameCycle.startGame(table = gameTable, hand = playerHand)

        //stage.isDebugAll = true
    }
    private fun createTimer() {
        timerLabel = Label("", GameSkinFactory.createTimerLabelSkin())
        timerLabel.setPosition(40f, stage.viewport.worldHeight - 80f)
        stage.addActor(timerLabel)

        gameTimer = GameTimer(label = timerLabel, scope = GameCycle.getGameScope())
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
        GameCycle.stopGame()
    }
}
