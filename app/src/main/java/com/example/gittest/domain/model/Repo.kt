package com.example.gittest.domain.model

data class Repo(
    val id: String,
    val name: String,
    val description: String? = null,
    val language: String? = null,
)
