package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table

class GameTable: Table() {
    init {
        setFillParent(true)
        pad(20f)

        add(createOpponentArea())
            .height(150f)
            .fillX()
            .row()
    }

    private fun createOpponentArea(): Table {
        val opponentArea = Table()

        for (col in 0..4){
            val slot = SlotActor(col)
        }
        opponentArea.add()




        return opponentArea
    }

    private fun createButtleArea() {

    }

    private fun createPlayerArea() {

    }
}
