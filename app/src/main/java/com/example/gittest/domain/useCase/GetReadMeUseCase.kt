package com.example.gittest.domain.useCase

import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.repository.AppRepository

class GetReadMeUseCase(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): Resource<String> = appRepository.getRepositoryReadme(
        ownerName,
        repositoryName,
        branchName
    )
}