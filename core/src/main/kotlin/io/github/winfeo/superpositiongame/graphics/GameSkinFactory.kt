package io.github.winfeo.superpositiongame.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label

object GameSkinFactory {

    fun createTimerLabelSkin(): Label.LabelStyle {
        val fontScale= 1.5f
        val fontColor = Color.WHITE
        val font = BitmapFont()
        font.data.setScale(fontScale)
        return Label.LabelStyle(font, fontColor)
    }

    ///TODO добавить скин для диалога сюда
}
