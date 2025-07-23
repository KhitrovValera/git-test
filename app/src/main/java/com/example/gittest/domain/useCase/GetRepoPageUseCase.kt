package com.example.gittest.domain.useCase

data class GetRepoPageUseCase(
    val getRepoDetailsUseCase: GetRepoDetailsUseCase,
    val getReadMeUseCase: GetReadMeUseCase
)
