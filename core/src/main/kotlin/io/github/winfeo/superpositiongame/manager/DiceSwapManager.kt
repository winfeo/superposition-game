package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.SlotActorStates
import io.github.winfeo.superpositiongame.actor.dice.DiceActor
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.game.GameCycle

class DiceSwapManager() {

    private var firstDice: DiceActor? = null
    private var secondDice: DiceActor? = null

    private var isSelecting = false

    private var allDices = mutableListOf<DiceActor>()
    private val stage: Stage = GameConfig.stage


    fun applyEffect() {
        if (isSelecting) return

        isSelecting = true
        firstDice = null
        secondDice = null
        allDices.clear()

        findAllDicesOnTable(stage.root)
        println("Отладка. Нашли дайсов: ${allDices.size}")
        setUpDiceClickListeners()
    }

    private fun findAllDicesOnTable(actor: Actor) {
        if (actor is DiceActor) {
            allDices.add(actor)
            actor.enableTouchableEffect()
        }
        if (actor is Group) {
            actor.children.forEach { child ->
                findAllDicesOnTable(child)
            }
        }
    }

    private fun setUpDiceClickListeners() {
        allDices.forEach { dice ->
            dice.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    handleDiceClick(dice)
                }
            })
        }
    }

    private fun handleDiceClick(dice: DiceActor) {
        if (isSelecting) {
            onDiceClicked(dice)
        }
    }

    fun onDiceClicked(dice: DiceActor): Boolean {
        if (!isSelecting) return false

        return when {
            firstDice == null -> {
                firstDice = dice
                (firstDice!!.parent as SlotActor).setState(SlotActorStates.HOVERED_CAN_PLACE)
                true
            }
            secondDice == null && dice != firstDice -> {
                secondDice = dice
                (firstDice!!.parent as SlotActor).setState(SlotActorStates.NO_ACTION)
                swapDices()
                GameCycle.playerMoveController.moveMade() //TODO колбэками сделать?
                true
            }
            else -> false
        }
    }

    private fun swapDices() {
        val firstDiceState = firstDice!!.dice.state
        val secondDiceState = secondDice!!.dice.state

        firstDice!!.changeState(secondDiceState)
        DiceChangerManager.changeDiceStateColor(firstDice!!)
        secondDice!!.changeState(firstDiceState)
        DiceChangerManager.changeDiceStateColor(secondDice!!)
        finishSelection()
        println("Отладка. Успешный обмен")
    }

    private fun finishSelection() {
        isSelecting = false
        firstDice = null
        secondDice = null
        allDices.forEach { it.disableTouchableEffect() }
        allDices.clear()
    }
}
