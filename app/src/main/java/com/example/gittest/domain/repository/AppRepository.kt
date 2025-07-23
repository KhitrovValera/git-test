package com.example.gittest.domain.repository

import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.model.Repo
import com.example.gittest.domain.model.RepoDetails
import com.example.gittest.domain.model.UserInfo

interface AppRepository {
    suspend fun getRepositories(): Resource<List<Repo>>

    suspend fun getRepository(
        repoId: String
    ): Resource<RepoDetails>

    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): Resource<String>

    suspend fun signIn(): Resource<UserInfo>
}