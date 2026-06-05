package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.UserApi

class UserRepository(
    private val api: UserApi
) {
    suspend fun getUserStats(userId: Long): Result<AuthorisedUserDTO> {
        return try {
            val user = api.getUserById(userId)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
