package com.example.gittest.domain.useCase

import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.model.Repo
import com.example.gittest.domain.repository.AppRepository

class GetReposUseCase(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(): Resource<List<Repo>> = appRepository.getRepositories()
}