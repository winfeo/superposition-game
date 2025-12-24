package io.github.winfeo.superpositiongame.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import space.earlygrey.shapedrawer.ShapeDrawer

object GraphicsUtils {
    //для отрисовки границ слотоа карт
    lateinit var shapeDrawer: ShapeDrawer

    fun initShapeDrawer(batch: SpriteBatch) {
        val texture = createSimpleTexture()
        shapeDrawer = ShapeDrawer(batch, TextureRegion(texture))
    }

    private fun createSimpleTexture(): Texture  {
        val pixmap = Pixmap(1,1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.GOLD)
        pixmap.drawPixel(0,0)
        val texture = Texture(pixmap)
        pixmap.dispose()
        return texture
    }
}
