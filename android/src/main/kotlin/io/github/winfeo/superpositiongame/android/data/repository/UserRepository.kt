package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.UpdateUserDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.UserApi
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

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

    suspend fun updateUser(updateUserDTO: UpdateUserDTO): Result<AuthorisedUserDTO> {
        return try {
            val user = api.updateUser(updateUserDTO)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(extractErrorMessage(e)))
        }
    }

    suspend fun updateEmail(
        userId: Long,
        newEmail: String
    ): Result<AuthorisedUserDTO> {
        return try {
            val currentUser = api.getUserById(userId)
            val updateDTO = UpdateUserDTO(
                id = userId,
                nickname = currentUser.nickname,
                email = newEmail
            )
            val updatedUser = api.updateUser(updateDTO)
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(Exception(extractErrorMessage(e)))
        }
    }

    suspend fun deleteAccount(userId: Long): Result<Unit> {
        return try {
            api.deleteUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(extractErrorMessage(e)))
        }
    }

    private suspend fun extractErrorMessage(e: Exception): String {
        return when (e) {
            is ClientRequestException -> {
                try { e.response.bodyAsText() }
                catch (_: Exception) { "Ошибка запроса" }
            }
            is ServerResponseException -> {
                try { e.response.bodyAsText() }
                catch (_: Exception) { "Ошибка сервера" }
            }
            is ConnectException -> "Нет подключения к серверу"
            is UnknownHostException -> "Нет подключения к серверу"
            is IOException -> "Нет подключения к серверу"
            else -> "${e.message}"
        }
    }
}
