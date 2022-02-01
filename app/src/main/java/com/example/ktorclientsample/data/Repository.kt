package com.example.ktorclientsample.data

import io.ktor.client.*

interface Repository {

    suspend fun getUsers(): RepositoryImpl.NetworkEvents

}