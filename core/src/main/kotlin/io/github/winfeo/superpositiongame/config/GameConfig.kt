package io.github.winfeo.superpositiongame.config

import com.badlogic.gdx.scenes.scene2d.Stage

///TODO Разделить на отдельный файлы констант? или сделать файл состояний с разными конфигурациями?
object GameConfig {
    ///TODO сделать просто геттеры для получения значения, а не дублировать переменные?
    private var _screenWidth: Float = 0f
    private var _screenHeight: Float = 0f
    val screenWidth: Float get() = _screenWidth
    val screenHeight: Float get() = _screenHeight

    private var _cardWidth: Float = 0f
    private var _cardHeight: Float = 0f
    val cardWidth: Float get() = _cardWidth
    val cardHeight: Float get() = _cardHeight

    ///TODO убрать из констант? Обновлять во время изменения размера экрана? Сделать приватной
    lateinit var stage: Stage

    fun init(screenWidth: Float, screenHeight: Float, stage: Stage) {
        _screenWidth = screenWidth
        _screenHeight = screenHeight
        _cardWidth = _screenWidth * CARD_WIDTH_PERCENT
        _cardHeight = _screenWidth * CARD_WIDTH_PERCENT * CARD_HEIGHT_RATIO
        this.stage = stage
    }


    ///TODO сделать инициализацию констант в отдельном методе при старте игры? сделать приватными константы?
    private const val DICE_SIZE_PERCENT = 0.55f //сторона кубита
    private const val DICE_PADDING_PERCENT = 0.1f
    private const val CARD_WIDTH_PERCENT = 0.1f //ширина карт
    private const val CARD_HEIGHT_RATIO = 1.625f //высота карт
    private const val TABLE_PADDING_PERCENT = 0.01f //паддинг между таблицами
    private const val CARD_PADDING_PERCENT = 0.02f //паддинг между картами
    private const val CARD_BORDER_THICKNESS_PERCENT = 0.02f //толщина границы рамки карты
    private const val CARD_BORDER_RADIUS_RATIO = 0.06f //скургление границы рамки карты
    private const val CARDS_IN_HAND = 6 //кол-во карт игрока
    private const val SLOTS_ON_TABLE = 4 //кол-во слотов на столе (задание)
    private const val MIN_DRAG_ZONE = 10f //мин. расстояние для начала драга карты
    private const val DOUBLE_TAP_INTERVAL_TIME = 300L
    private const val MOVE_TIMER_INTERVAL = 30
    private const val CARD_CIRCLE_RADIUS = 800f
    private const val CARD_CIRCLE_ANGLE = 60f

    fun getMinDragZone(): Float = MIN_DRAG_ZONE
    fun getTablesPadding(): Float {
        val tablePadding = _screenHeight * TABLE_PADDING_PERCENT
        val dicePadding = getDiceSide() * DICE_PADDING_PERCENT
        return tablePadding + dicePadding
    }
    fun getCardsPadding(): Float {
        val tablePadding = _screenWidth * CARD_PADDING_PERCENT
        val dicePadding = getDiceSide() / 2 + cardWidth * DICE_PADDING_PERCENT
        return tablePadding + dicePadding
    }
    fun getCardBorderThickness(): Float = cardWidth * CARD_BORDER_THICKNESS_PERCENT
    fun getCardBorderRadius(): Float = cardHeight * CARD_BORDER_RADIUS_RATIO
    fun getCardsInHandAmount(): Int = CARDS_IN_HAND
    fun getSlotsOnTableAmount(): Int = SLOTS_ON_TABLE
    fun getDiceSide(): Float = cardWidth * DICE_SIZE_PERCENT
    fun getDoubleTapIntervalTime() = DOUBLE_TAP_INTERVAL_TIME
    fun getTimerDuration() = MOVE_TIMER_INTERVAL
    fun getCardCircleRadius() = CARD_CIRCLE_RADIUS
    fun getCardCircleAngle() = CARD_CIRCLE_ANGLE


}
