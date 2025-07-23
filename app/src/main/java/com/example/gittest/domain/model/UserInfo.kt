package com.example.gittest.domain.model

data class UserInfo(
    val id: Int = 0,
    val login: String = "",
    val name: String? = "",
    val avatarUrl: String = "",
    val profileUrl: String = ""
)
