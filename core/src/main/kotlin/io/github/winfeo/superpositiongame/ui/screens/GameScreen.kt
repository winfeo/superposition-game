package io.github.winfeo.superpositiongame.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.managers.CardsAtlasManger
import io.github.winfeo.superpositiongame.ui.GameTable
import ktx.app.KtxScreen

class GameScreen : KtxScreen {
    private val stage = Stage()
    val batch = SpriteBatch()

    val card1 = CardsAtlasManger.getSprite("redcard1")
//    private val gameTable = GameTable

    override fun show() {
        super.show()

        val scale = 0.3f
        val scaledWidth = (card1?.width ?: 0f) * scale
        val scaledHeight = (card1?.height ?: 0f) * scale
        card1?.setBounds(100f, 100f, scaledWidth, scaledHeight)
        card1?.setOrigin(0f,0f)

        card1?.setPosition(1f, 1f)
    }

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(0f,0f,0f,0f)

        batch.begin()
        card1?.draw(batch)
        batch.end()
    }
}
