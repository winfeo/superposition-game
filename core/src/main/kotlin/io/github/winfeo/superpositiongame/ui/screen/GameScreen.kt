package io.github.winfeo.superpositiongame.ui.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.game.PlayerActionController
import io.github.winfeo.superpositiongame.graphics.BorderTexture
import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.manager.CardsAtlasManager
import io.github.winfeo.superpositiongame.manager.CardsDoubleTapManager
import io.github.winfeo.superpositiongame.manager.CardsDragAndDropManager
import io.github.winfeo.superpositiongame.manager.CardsLongPressManager
import io.github.winfeo.superpositiongame.manager.GameAssetsManager
import io.github.winfeo.superpositiongame.manager.SwapSelectionManager
import io.github.winfeo.superpositiongame.ui.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.ui.actor.dice.DiceActorBuilder
import io.github.winfeo.superpositiongame.ui.screen.elements.CardsFan
import io.github.winfeo.superpositiongame.ui.screen.elements.GameTable
import io.github.winfeo.superpositiongame.ui.screen.elements.TurnLabel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import ktx.app.KtxScreen

//Игровой экран, MVI паттерн
class GameScreen(
    private val assetsManager: GameAssetsManager,
    private val playerId: String,
    private val getOpponentId: () -> String,
    private val dialogs: Dialogs,
    private val onMove: (Move) -> Unit,
    private val getGameState: () -> GameState,
    private val applyPendingState: () -> Unit
) : KtxScreen {
    private val cardActorBuilder = CardActorBuilder(assetsManager = assetsManager)
    private val diceActorBuilder = DiceActorBuilder(assetsManager = assetsManager)
    private val stage = Stage(ScreenViewport())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val swapManager: SwapSelectionManager by lazy {
        SwapSelectionManager(
            allSlotsProvider = {
                gameTable.getAllSlots()
            }
        )
    }

    private val doubleTapManager by lazy {
        CardsDoubleTapManager(
            controller = playerActionController,
            onCardConsumed = { card ->
                val gameState = getGameState()
                if (gameState.currentPlayerId == playerId) {
                    cardsFan.consumeCard(card)
                }
            }
        )
    }

    private val playerActionController = PlayerActionController(
        playerId = playerId,
        getOpponentId = getOpponentId,
        stage = stage,
        dialogs = dialogs,
        scope = scope,
        onMove = onMove,
        getGameState = getGameState,
        swapManager = swapManager
    )
    private val dragManager = CardsDragAndDropManager(playerActionController)
//    private val doubleTapManager = CardsDoubleTapManager(playerActionController)
    private val longPressManager = CardsLongPressManager(dialogs)

    private val backgroundTexture by lazy {
        Texture(Gdx.files.internal("background_blured3.png"))
    }

    private val turnLabel: TurnLabel by lazy {
        TurnLabel(playerId = playerId)
    }

    private val cardsFan: CardsFan by lazy {
        CardsFan(
            playerId = playerId,
            stage = stage,
            dragManager = dragManager,
            doubleTapManager = doubleTapManager,
            longPressManager = longPressManager,
            cardActorBuilder = cardActorBuilder
        )
    }

    private val gameTable: GameTable by lazy {
        GameTable(
            playerId = playerId,
            dragManager = dragManager,
            cardActorBuilder = cardActorBuilder,
            diceActorBuilder = diceActorBuilder
        )
    }

//    private lateinit var timerLabel: Label
//    private lateinit var gameTimer: GameTimer

    init {
         //TODO тоже перенести в show?
    }

    fun renderState(newState: GameState) {
        gameTable.render(newState)
        cardsFan.render(newState)
        turnLabel.render(newState.currentPlayerId)
    }

    override fun show() {
        super.show()
        GameConfig.init(stage = stage)
        Gdx.input.inputProcessor = stage

        stage.clear()
        stage.addActor(gameTable)

        turnLabel.setPosition( ///TODO переделать
            400f,
            stage.height - 400f
        )
        stage.addActor(turnLabel)
//        stage.isDebugAll = true

    }

    override fun render(delta: Float) {
//        super.render(delta)
        applyPendingState()
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val batch = stage.batch
        batch.begin()
        batch.color = Color.WHITE
        val textureWidth = backgroundTexture.width.toFloat()
        val textureHeight = backgroundTexture.height.toFloat()
        val screenWidth = stage.width
        val screenHeight = stage.height
        val scale = maxOf(screenWidth / textureWidth, screenHeight / textureHeight)
        val drawWidth = textureWidth * scale
        val drawHeight = textureHeight * scale
        val x = (screenWidth - drawWidth) / 2f
        val y = (screenHeight - drawHeight) / 2f
        batch.draw(
            backgroundTexture,
            x,
            y,
            drawWidth,
            drawHeight
        )
        batch.end()

        stage.act(delta)
        stage.draw()
        Gdx.app.log("CARD", "акторов на сцене = ${stage.actors.size}")
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        super.dispose()

        BorderTexture.clear()
        backgroundTexture.dispose()
        stage.dispose()
        dragManager.clear()
        doubleTapManager.clear()
        longPressManager.clear()
        scope.cancel()
    }
}
