package com.example.ktorclientsample.data

import com.example.ktorclientsample.domain.data.User
import com.example.ktorclientsample.model.RemoteUser
import io.ktor.client.*
import io.ktor.client.request.*

class RepositoryImpl constructor(
    private val httpClient: HttpClient
) : Repository {

    sealed class NetworkEvents {
        data class Success(val data: List<User>) : NetworkEvents()
        data class Failure(val message: String) : NetworkEvents()
    }

    override suspend fun getUsers(): NetworkEvents {
        return try {
            val response = httpClient.get<List<RemoteUser>>(ApiService.Endpoint.USERS_EP.url)
            NetworkEvents.Success(response.map { it.toUser() })
        } catch (ex: Exception) {
            ex.printStackTrace()
            NetworkEvents.Failure(ex.message ?: "An error occurred")
        }
    }
}