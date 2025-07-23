package com.example.gittest.data.remote.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class RepoDetailsDto(
    val id: Long = 0,
    val name: String = "",
    val full_name: String = "",
    val owner: OwnerDto = OwnerDto(),
    val description: String? = null,
    val html_url: String = "",
    val stargazers_count: Int = 0,
    val watchers_count: Int = 0,
    val forks_count: Int = 0,
    val open_issues_count: Int = 0,
    val default_branch: String = "main",
    val language: String? = null,
    val license: LicenseDto?,
    val private: Boolean = true
)