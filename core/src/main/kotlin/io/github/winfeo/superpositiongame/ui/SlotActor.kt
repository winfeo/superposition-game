package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.configs.GameConfig
import io.github.winfeo.superpositiongame.ui.screens.GameScreen
import io.github.winfeo.superpositiongame.utils.GraphicsUtils

// Класс-ячейка таблицы для помещения карты на игровое поле
class SlotActor(): Table() {
    ///TODO может быть сделать фабрику объектов? Чтобы каждый раз не тратить ресурсы на каждй новый объект
    private val cardWidth = GameConfig.cardWidth
    private val cardHeight = GameConfig.cardHeight

    private var state = SlotActorStates.NO_ACTION
    private var slotColor = Color.GOLD

    ///TODO подумать, может динамически параметр рассчитывать как-то от размеров
    val bordersThickness = 2f
    val cornerRadius = 5f


    fun setState(newState: SlotActorStates) {
        state = newState
        setBordersColor()
    }

    fun setBordersColor() {
        slotColor = when (state) {
            SlotActorStates.NO_ACTION -> Color.GOLD
            SlotActorStates.HOVERED_CAN_PLACE -> Color.GREEN
            SlotActorStates.HOVERED_CANT_PLACE -> Color.RED
        }
    }

    init {
        defaults()
            .minSize(cardWidth, cardHeight)
            .prefSize(cardWidth, cardHeight)
            .maxSize(cardWidth,cardHeight)
        pad(5f) //отступ от границы
//        background = createBorders()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        drawBorder(batch, parentAlpha)
    }

    private fun drawBorder(batch: Batch, parentAlpha: Float) {
        val shapeDrawer = GraphicsUtils.shapeDrawer
        val x = this.x
        val y = this.y
        val width = this.width
        val height = this.height
        val color = Color(slotColor)
        color.a *= parentAlpha
        shapeDrawer.setColor(color)


        shapeDrawer.rectangle(x,y,width,height, bordersThickness)
    }

    fun placeCard(card: CardActor) {
        //currentCard = card
        children.filterIsInstance<Image>().forEach { it.remove() }
        clearChildren()
        add(card).size(card.width, card.height).center()
    }

//    fun createBorders(): TextureRegionDrawable {
//
//        ///TODO рамка должна менять цвет в зависимости от действия!!!
//        val pixmap = Pixmap(cardWidth.toInt(), cardHeight.toInt(), Pixmap.Format.RGBA8888)
//
//        pixmap.setColor(Color.CLEAR)
//        pixmap.fill()
//
//        pixmap.setColor(Color.GOLD)
//        pixmap.fillRectangle( //верх
//            cornerRadius,
//            0,
//            pixmap.width - cornerRadius * 2,
//            bordersThickness
//        )
//        pixmap.fillRectangle( //низ
//            cornerRadius,
//            pixmap.height - bordersThickness,
//            pixmap.width - cornerRadius * 2,
//            bordersThickness
//        )
//        pixmap.fillRectangle( //лево
//            0,
//            cornerRadius,
//            bordersThickness,
//            pixmap.height - 2 * cornerRadius
//        )
//        pixmap.fillRectangle( //право
//            pixmap.width - bordersThickness,
//            cornerRadius,
//            bordersThickness,
//            pixmap.height - 2 * cornerRadius
//        )
//
//        ///TODO добавить потом сглаживания углов для рамки
////        pixmap.fillCircle(cornerRadius,cornerRadius,cornerRadius)
////        pixmap.fillCircle(pixmap.width - cornerRadius - 1, cornerRadius, cornerRadius)
////        pixmap.fillCircle(cornerRadius, pixmap.height - cornerRadius - 1, cornerRadius)
////        pixmap.fillCircle(pixmap.width - cornerRadius - 1, pixmap.height - cornerRadius - 1, cornerRadius)
//
//        val texture = Texture(pixmap)
//        pixmap.dispose()
//        return TextureRegionDrawable(texture)
//    }


}
