package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DOUBLE_TAP")
data class DoubleTapEffectDto(
    override val playerId: String,
    val cardId: String
): MoveDto()
