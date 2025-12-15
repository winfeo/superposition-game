package io.github.winfeo.superpositiongame.configs

///TODO Разделить на отдельный файлы констант? или сделать файл состояний с разными конфигурациями?
object GameConfig {
    private var _screenWidth: Float = 0f
    private var _screenHeight: Float = 0f
    val screenWidth: Float get() = _screenWidth
    val screenHeight: Float get() = _screenHeight

    private var _cardWidth: Float = 0f
    private var _cardHeight: Float = 0f
    val cardWidth: Float get() = _cardWidth
    val cardHeight: Float get() = _cardHeight

    fun init(screenWidth: Float, screenHeight: Float) {
        _screenWidth = screenWidth
        _screenHeight = screenHeight
        _cardWidth = _screenWidth * CARD_WIDTH_PERCENT
        _cardHeight = _screenWidth * CARD_WIDTH_PERCENT * CARD_HEIGHT_RATIO
    }


    ///TODO сделать инициализацию констант в отдельном методе при старте игры? сделать приватными константы?
    const val CARD_WIDTH_PERCENT = 0.1f //ширина карт
    const val CARD_HEIGHT_RATIO = 1.625f //высота карт
    const val TABLE_PADDING_PERCENT = 0.01f //паддинг между таблицами
    const val CARD_PADDING_PERCENT = 0.02f //паддинг между картами

    const val CARDS_IN_HAND = 6 //кол-во карт игроков
    const val CARD_ON_TABLE = 4 //кол-во слотов на столе (задание)

    fun getTablesPadding(): Float = _screenHeight * TABLE_PADDING_PERCENT

    fun getCardsPadding(): Float = _screenWidth * CARD_PADDING_PERCENT

}
