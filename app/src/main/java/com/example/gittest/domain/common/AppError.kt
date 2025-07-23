package com.example.gittest.domain.common

sealed class AppError {
    object NetworkError : AppError()
    data class ApiError(val code: Int? = null) : AppError()
    object UnknownError : AppError()
}