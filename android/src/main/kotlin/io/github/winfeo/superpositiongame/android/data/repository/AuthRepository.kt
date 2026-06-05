package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.NewUserDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.AuthApi
import io.github.winfeo.superpositiongame.android.data.source.rest.UserSession
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

class AuthRepository(
    private val authApi: AuthApi
) {
    suspend fun register(dto: NewUserDTO): Result<AuthorisedUserDTO> {
        return try {
            val user = authApi.register(dto)
            UserSession.login(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(extractErrorMessage(e)))
        }
    }

    suspend fun login(dto: NewUserDTO): Result<AuthorisedUserDTO> {
        return try {
            val user = authApi.login(dto)
            UserSession.login(user)
            Result.success(user)
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
