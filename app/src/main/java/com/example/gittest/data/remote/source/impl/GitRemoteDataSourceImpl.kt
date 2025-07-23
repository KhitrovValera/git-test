package com.example.gittest.data.remote.source.impl

import com.example.gittest.data.remote.api.GitHubService
import com.example.gittest.data.remote.model.ReadMeDto
import com.example.gittest.data.remote.model.RepoDetailsDto
import com.example.gittest.data.remote.model.RepoDto
import com.example.gittest.data.remote.model.UserInfoDto
import com.example.gittest.data.remote.result.ApiCaller
import com.example.gittest.data.remote.result.NetworkResult
import com.example.gittest.data.remote.source.GitRemoteDataSource
import javax.inject.Inject

class GitRemoteDataSourceImpl @Inject constructor(
    private val api: GitHubService,
    private val apiCaller: ApiCaller
): GitRemoteDataSource {
    override suspend fun getRepositories(): NetworkResult<List<RepoDto>> {
        return apiCaller.safeApiCall { api.getRepositories() }
    }

    override suspend fun getRepository(
        repoId: String
    ): NetworkResult<RepoDetailsDto> {
        return apiCaller.safeApiCall { api.getRepository(repoId) }
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): NetworkResult<ReadMeDto> {
        return apiCaller.safeApiCall { api.getReadme(ownerName, repositoryName, branchName) }
    }

    override suspend fun signIn(): NetworkResult<UserInfoDto> {
        return apiCaller.safeApiCall { api.signIn() }
    }
}