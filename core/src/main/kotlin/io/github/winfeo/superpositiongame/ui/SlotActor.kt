package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.configs.GameConfig

// Класс-ячейка для помещения карты на игровое поле
// Передаются координаты (позиция) карты в конкретном слоте таблицы
class SlotActor(): Table() {
    ///TODO может быть сделать фабрику объектов? Чтобы каждый раз не тратить ресурсы на каждй новый объект
    private val cardWidth = GameConfig.cardWidth
    private val cardHeight = GameConfig.cardHeight

    init {
        defaults()
            .minSize(cardWidth + 50f, cardHeight + 50f)
            .prefSize(cardWidth + 50f, cardHeight + 50f)
            .maxSize(cardWidth + 50f,cardHeight + 50f)
        pad(5f)
        //background = createBorders()
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
