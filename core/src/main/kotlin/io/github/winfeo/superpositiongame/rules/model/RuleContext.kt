package io.github.winfeo.superpositiongame.rules.model

import io.github.winfeo.superpositiongame.actor.SlotActor
import io.github.winfeo.superpositiongame.actor.card.CardActor

data class RuleContext(
    val card: CardActor,
    val targetSlot: SlotActor
)
