package io.github.winfeo.superpositiongame.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InvitationDto(
    val senderId: String = "",
    val receiverId: String = "",
    val sendTime: String = ""
)
