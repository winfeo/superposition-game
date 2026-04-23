package io.github.winfeo.superpositiongame.graphics

import io.github.winfeo.superpositiongame.model.dice.DiceState

//Диалоги во время игры (имплементация в android-модуле)
interface Dialogs {
    //Для Rotate-карты
    fun showRotateCardDialog(
        availableStates: List<DiceState>,
        onStateSelected: (DiceState) -> Unit
    )
}
