package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.configs.GameConfig

// Класс-ячейка для помещения карты на игровое поле
// Передаются координаты (позиция) карты в конкретном слоте таблицы
class SlotActor(
    val cardWidth: Float,
    val cardHeight: Float,
    val row: Int = 0,
    val col: Int = 0
): Table() {
    //private var currentCard: CardActor? = null
    //private val background: Image = Image().apply { color = Color.DARK_GRAY }
//    private val WIDTH = 80f //80
//    private val HEIGHT = 130f //130
//    private val width = Gdx.graphics.width.toFloat() * GameConfig.CARD_WIDTH_PERCENT
//    private val height = Gdx.graphics.height.toFloat() * GameConfig.CARD_HEIGHT_RATIO

    init {
        defaults()
            .minSize(cardWidth, cardHeight)
            .prefSize(cardWidth, cardHeight)
            .maxSize(cardWidth,cardHeight)
        pad(5f)
        background = createBorders()
    }

    fun placeCard(card: CardActor) {
        //currentCard = card
        children.filterIsInstance<Image>().forEach { it.remove() }
        clearChildren()
        add(card).size(card.width, card.height).center()
    }

    fun createBorders(): TextureRegionDrawable {
        ///TODO создаём рамку вокруг ячейки (заливаем просто цветом область чуть больше), подумать как улучшить (кэш?)

        val borderColor = Color.GOLD
        val pixmap = Pixmap(cardWidth.toInt(), cardHeight.toInt(), Pixmap.Format.RGBA8888)
        pixmap.setColor(borderColor)
        pixmap.fill()
        val texture = Texture(pixmap)
        pixmap.dispose()
        return TextureRegionDrawable(texture)
    }


}
