package com.example.gittest.domain.useCase

import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.model.UserInfo
import com.example.gittest.domain.repository.AppRepository

class SignInUseCase(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(): Resource<UserInfo> = appRepository.signIn()
}