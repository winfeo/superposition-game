package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.GuestResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post

class GuestApi(
    private val client: HttpClient
) {
//    private val HOST: String = "http://91.237.249.20:8080"
    private val HOST: String = "http://10.0.2.2:8080"

    suspend fun createGuest(): GuestResponse {
        return client.post("$HOST/api/guest/create").body()
    }
}
