package io.github.winfeo.superpositiongame.rules.model

import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.model.Card

data class RuleContext(
    val card: Card,
    val targetSlot: SlotActor
)
