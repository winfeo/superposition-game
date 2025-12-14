package io.github.winfeo.superpositiongame.configs

import io.github.winfeo.superpositiongame.configs.GameConfig.CARD_WIDTH_PERCENT

///TODO Разделить на отдельный файлы констант? или сделать файл состояний с разными конфигурациями?
object GameConfig {
    ///TODO сделать инициализацию констант в отдельном методе при старте игры? сделать приватными константы?
    const val CARD_WIDTH_PERCENT = 0.1f //ширина карт
    const val CARD_HEIGHT_RATIO = 1.625f //высота карт
    const val TABLE_PADDING_PERCENT = 0.01f //паддинг между таблицами
    const val CARD_PADDING_PERCENT = 0.02f //паддинг между картами

    const val CARDS_IN_HAND = 6 //кол-во карт игроков
    const val CARD_ON_TABLE = 4 //кол-во слотов на столе (задание)

    fun getCardWidth(screenWidth: Float): Float = screenWidth * CARD_WIDTH_PERCENT
    fun getCardHeight(screenWidth: Float): Float = screenWidth * CARD_WIDTH_PERCENT * CARD_HEIGHT_RATIO
    fun getTablesPadding(screenHeight: Float): Float = screenHeight * TABLE_PADDING_PERCENT

    fun getCardsPadding(screenWidth: Float): Float = screenWidth * CARD_PADDING_PERCENT

}
