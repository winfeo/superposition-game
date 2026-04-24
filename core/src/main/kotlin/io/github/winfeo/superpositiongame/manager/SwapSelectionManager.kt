package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import io.github.winfeo.superpositiongame.ui.actor.SlotActor
import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates

class SwapSelectionManager(
    private val allSlotsProvider: () -> List<SlotActor>
) {
    private var firstSelected: SlotActor? = null
    private var isSelectionMode = false

    fun startSelection(
        onSelected: (SlotActor, SlotActor) -> Unit
    ) {
        reset()

        isSelectionMode = true

        allSlotsProvider().forEach { slot ->
            slot.addListener(object: InputListener() {
                override fun touchDown(
                    event: InputEvent?,
                    x: Float,
                    y: Float,
                    pointer: Int,
                    button: Int
                ): Boolean {
                    handleSlotClick(slot, onSelected)
                    return true
                }
            })
        }
    }

    private fun handleSlotClick(
        slot: SlotActor,
        onSelected: (SlotActor, SlotActor) -> Unit
    ) {
        if (!isSelectionMode) return

        if (firstSelected == null) {
            firstSelected = slot
            slot.setNewState(SlotActorStates.HOVERED_CAN_PLACE)
            return
        }

        val first = firstSelected ?: return
        val second = slot

        if (first == second) return

        onSelected(first, second)
        reset()
    }

    fun reset() {
        firstSelected = null
        isSelectionMode = false

        allSlotsProvider().forEach {
            it.setNewState(SlotActorStates.NO_ACTION)
            it.clearListeners()
        }
    }
}
