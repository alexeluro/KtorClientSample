package com.example.ktorclientsample.model

import com.example.ktorclientsample.domain.data.User
import kotlinx.serialization.Serializable

@Serializable
data class RemoteUser(
    val id: Long,
    val name: String?,
    val username: String?,
    val email: String?,
    val phone: String?
){
    fun toUser() = User(
        name = name ?: "",
        email = email ?: "",
        phone = phone ?: ""
    )
}