package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class UserApi(
    private val client: HttpClient
) {
//    private val HOST: String = "http://91.237.249.20:8080"
    private val HOST: String = "http://10.0.2.2:8080"

    suspend fun getUserById(userId: Long): AuthorisedUserDTO {
        return client.get("$HOST/api/users/$userId").body()
    }
}
