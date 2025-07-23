package com.example.gittest.domain.model

data class RepoDetails(
    val id: String,
    val name: String,
    val fullName: String = "",
    val ownerLogin: String,
    val description: String? = null,
    val htmlUrl: String,
    val stargazersCount: Int = 0,
    val watchersCount: Int = 0,
    val forksCount: Int = 0,
    val openIssuesCount: Int = 0,
    val language: String? = null,
    val private: Boolean = true,
    val defaultBranch: String,
    val licenseName: String?
)
