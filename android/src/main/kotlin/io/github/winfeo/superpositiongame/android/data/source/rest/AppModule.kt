package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.repository.AuthRepository
import io.github.winfeo.superpositiongame.android.data.repository.GameHistoryRepository
import io.github.winfeo.superpositiongame.android.data.repository.GuestRepository
import io.github.winfeo.superpositiongame.android.data.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AppModule {
    private val client by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    val authApi by lazy { AuthApi(client) }
    val authRepository by lazy { AuthRepository(authApi) }
    val gameHistoryApi by lazy { GameHistoryApi(client) }
    val gameHistoryRepository by lazy { GameHistoryRepository(gameHistoryApi) }
    val guestApi by lazy { GuestApi(client) }
    val guestRepository by lazy { GuestRepository(guestApi) }
    val userApi by lazy { UserApi(client) }
    val userRepository by lazy { UserRepository(userApi) }
}
