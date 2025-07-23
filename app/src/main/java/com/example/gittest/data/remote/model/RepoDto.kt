package com.example.gittest.data.remote.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class RepoDto(
    val id: Long = 0,
    val name: String = "",
    val full_name: String = "",
    val description: String? = null,
    val language: String? = null,
    val owner: OwnerDto = OwnerDto()
)
