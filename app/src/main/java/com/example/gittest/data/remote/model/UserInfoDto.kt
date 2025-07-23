package com.example.gittest.data.remote.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class UserInfoDto(
    val login: String = "",
    val id: Int = 0,
    val avatar_url: String = "",
    val name: String? = "",
    val email: String? = null,
    val html_url: String = ""
)