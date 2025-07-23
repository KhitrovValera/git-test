package com.example.gittest.data.remote.source

import com.example.gittest.data.remote.model.ReadMeDto
import com.example.gittest.data.remote.model.RepoDetailsDto
import com.example.gittest.data.remote.model.RepoDto
import com.example.gittest.data.remote.model.UserInfoDto
import com.example.gittest.data.remote.result.NetworkResult

interface GitRemoteDataSource {
    suspend fun getRepositories(): NetworkResult<List<RepoDto>>

    suspend fun getRepository(
        repoId: String
    ): NetworkResult<RepoDetailsDto>

    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): NetworkResult<ReadMeDto>

    suspend fun signIn(): NetworkResult<UserInfoDto>
}