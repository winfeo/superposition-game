package io.github.winfeo.superpositiongame.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.DiceAtlasManager
import io.github.winfeo.superpositiongame.model.dice.DiceState
import kotlinx.coroutines.suspendCancellableCoroutine

//Выбор состояния кубита для Rotate гейтов (диалоговое окно)
///TODO не переисовывается при изменении размера экрана
object RotateSelectionDialog {
    private lateinit var skin: Skin
    private lateinit var stage: Stage

    fun init(stage: Stage) {
        this.stage = stage
        createSimpleSkin()
    }

    private fun createSimpleSkin() {
        skin = Skin()

        val font = BitmapFont()
        skin.add("default-font", font)

        fun createColorDrawable(color: Color): TextureRegionDrawable {
            val pixmap = Pixmap(stage.width.toInt(), stage.height.toInt(), Pixmap.Format.RGBA8888)
            pixmap.setColor(color)
            pixmap.fill()
            val texture = Texture(pixmap)
            pixmap.dispose()
            return TextureRegionDrawable(texture)
        }

        val windowStyle = Window.WindowStyle().apply {
            this.titleFont = skin.getFont("default-font")
            this.titleFontColor = Color.WHITE
            this.background = createColorDrawable(Color(0.1f, 0.1f, 0.1f, 0.95f))
        }
        skin.add("default", windowStyle)

        val labelStyle = Label.LabelStyle().apply {
            this.font = skin.getFont("default-font")
            this.fontColor = Color.WHITE
        }
        skin.add("default", labelStyle)
    }

    suspend fun show(
        stage: Stage,
        availableStates: List<DiceState>
    ): DiceState = suspendCancellableCoroutine { cont ->

        val dialog = Dialog("", skin).apply {
            setModal(true)
            setMovable(false)
        }

        val gridTable = Table()
        gridTable.defaults().pad(GameConfig.getCardsPadding())

        availableStates.forEach { state ->
            val stateTexture = DiceAtlasManager.getStateTexture(state.textureId)
            val image = Image(stateTexture)

            image.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    if (cont.isActive) {
                        cont.resume(state) {}
                    }
                    dialog.hide()
                }
            })

            val diceSide = GameConfig.cardWidth
            gridTable.add(image).size(diceSide, diceSide)
        }

        dialog.contentTable.add(gridTable)
        dialog.show(stage)
    }
}
