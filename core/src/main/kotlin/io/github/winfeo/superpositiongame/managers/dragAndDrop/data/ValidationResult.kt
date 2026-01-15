package io.github.winfeo.superpositiongame.managers.dragAndDrop.data

import com.badlogic.gdx.graphics.Color

data class ValidationResult(
    val canPlace: Boolean,
    val message: String? = null,
    val activeColor: Color? = null
)
