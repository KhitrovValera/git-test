package com.example.gittest.domain.common

sealed class Resource<out T> {
    data class Success<T>(val data: T): Resource<T>()
    data class Error(val error: AppError): Resource<Nothing>()
}