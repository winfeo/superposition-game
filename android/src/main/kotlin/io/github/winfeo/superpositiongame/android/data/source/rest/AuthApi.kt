package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.NewUserDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthApi(
    private val client: HttpClient
) {
//    private val HOST: String = "http://91.237.249.20:8080"
    private val HOST: String = "http://10.0.2.2:8080"

    suspend fun register(dto: NewUserDTO): AuthorisedUserDTO {
        val response = client.post("$HOST/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess()) {
            throw Exception(response.bodyAsText())
        }

        return response.body()
    }

    suspend fun login(dto: NewUserDTO): AuthorisedUserDTO {
        val response = client.post("$HOST/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess()) {
            throw Exception(response.bodyAsText())
        }

        return response.body()
    }
}
