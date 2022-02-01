package com.example.ktorclientsample.data

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*

interface ApiService {

    sealed class Endpoint(val url: String) {
        object USERS_EP : Endpoint("$BASE_URL/users")
    }

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"
        fun build(): HttpClient {
            return HttpClient(CIO) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(
                        kotlinx.serialization.json.Json {
                            isLenient = false
                            ignoreUnknownKeys = true
                        }
                    )
                }
                install(Logging) {
                    logger = Logger.ANDROID
                    level = LogLevel.BODY
                }

                defaultRequest {
                    contentType(ContentType.Application.Json)
                }
            }
        }
    }

}