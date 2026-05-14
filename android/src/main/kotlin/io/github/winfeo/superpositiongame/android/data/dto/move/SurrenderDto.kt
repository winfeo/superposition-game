package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SURRENDER")
data class SurrenderDto(
    override val playerId: String
): MoveDto()

