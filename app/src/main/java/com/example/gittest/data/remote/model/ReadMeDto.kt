package com.example.gittest.data.remote.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class ReadMeDto(
    val name: String = "",
    val content: String = "",
    val encoding: String = ""
)
