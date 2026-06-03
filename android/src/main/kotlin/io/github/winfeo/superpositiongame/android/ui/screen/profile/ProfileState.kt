package io.github.winfeo.superpositiongame.android.ui.screen.profile

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO

data class ProfileState(
    val isAuthorized: Boolean = false,
    val user: AuthorisedUserDTO? = null
)
