package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorisedUserDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.UpdateUserDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserApi(
    private val client: HttpClient
) {
//    private val HOST: String = "http://91.237.249.20:8080"
    private val HOST: String = "http://10.0.2.2:8080"

    suspend fun getUserById(userId: Long): AuthorisedUserDTO {
        return client.get("$HOST/api/users/$userId").body()
    }

    suspend fun updateUser(updateUserDTO: UpdateUserDTO): AuthorisedUserDTO {
        return client.put("$HOST/api/users") {
            contentType(ContentType.Application.Json)
            setBody(updateUserDTO)
        }.body()
    }
}
