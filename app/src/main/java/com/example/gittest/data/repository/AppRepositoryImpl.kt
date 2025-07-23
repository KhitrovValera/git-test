package com.example.gittest.data.repository

import com.example.gittest.data.mapper.toDomain
import com.example.gittest.data.mapper.toResource
import com.example.gittest.data.remote.source.GitRemoteDataSource
import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.model.Repo
import com.example.gittest.domain.model.RepoDetails
import com.example.gittest.domain.model.UserInfo
import com.example.gittest.domain.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val gitRemoteDataSource: GitRemoteDataSource
): AppRepository {

    override suspend fun getRepositories(): Resource<List<Repo>> {
        return gitRemoteDataSource.getRepositories()
            .toResource { listDto -> listDto.toDomain() }
    }

    override suspend fun getRepository(repoId: String): Resource<RepoDetails> {
        return gitRemoteDataSource.getRepository(repoId)
            .toResource { dto -> dto.toDomain() }
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): Resource<String> {
        return gitRemoteDataSource.getRepositoryReadme(
            ownerName,
            repositoryName,
            branchName
        ).toResource { dto -> dto.toDomain()}
    }

    override suspend fun signIn(): Resource<UserInfo> {
        return gitRemoteDataSource.signIn()
            .toResource { dto -> dto.toDomain() }
    }
}