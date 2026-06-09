package io.github.winfeo.superpositiongame.android.data.source.rest

import android.content.Context
import io.github.winfeo.superpositiongame.android.data.repository.AuthRepository
import io.github.winfeo.superpositiongame.android.data.repository.GameHistoryRepository
import io.github.winfeo.superpositiongame.android.data.repository.GuestRepository
import io.github.winfeo.superpositiongame.android.data.repository.UserRepository
import io.github.winfeo.superpositiongame.android.ui.screen.settings.SettingsManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AppModule {
    private lateinit var appContext: Context
    private val client by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            expectSuccess = true
            defaultRequest {
                UserSession.token.value?.let { token ->
                    header("Authorization", "Bearer $token")
                }
            }
        }
    }

    fun init(context: Context) {
        appContext = context
    }

    val settingsManager by lazy { SettingsManager(appContext) }
    val tokenManager by lazy { TokenManager(appContext) }

    val authApi by lazy { AuthApi(client) }
    val authRepository by lazy { AuthRepository(authApi) }
    val gameHistoryApi by lazy { GameHistoryApi(client) }
    val gameHistoryRepository by lazy { GameHistoryRepository(gameHistoryApi) }
    val guestApi by lazy { GuestApi(client) }
    val guestRepository by lazy { GuestRepository(guestApi) }
    val userApi by lazy { UserApi(client) }
    val userRepository by lazy { UserRepository(userApi) }
}
