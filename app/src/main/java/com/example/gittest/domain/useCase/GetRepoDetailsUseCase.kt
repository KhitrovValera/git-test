package com.example.gittest.domain.useCase

import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.model.RepoDetails
import com.example.gittest.domain.repository.AppRepository

class GetRepoDetailsUseCase(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(repoId: String): Resource<RepoDetails> = appRepository.getRepository(repoId)
}