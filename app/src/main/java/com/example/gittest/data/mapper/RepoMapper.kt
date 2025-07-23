package com.example.gittest.data.mapper

import com.example.gittest.data.remote.model.RepoDto
import com.example.gittest.domain.model.Repo

fun RepoDto.toDomain(): Repo {
    return Repo(
        id = id.toString(),
        name = name,
        description = description,
        language = language
    )
}

fun List<RepoDto>.toDomain(): List<Repo> = this.map { it.toDomain() }