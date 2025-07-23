package com.example.gittest.data.remote.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class OwnerDto(
    val login: String = "",
    val id: Long = 0
)