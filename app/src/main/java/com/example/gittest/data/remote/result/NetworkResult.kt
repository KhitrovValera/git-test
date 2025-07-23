package com.example.gittest.data.remote.result

import com.example.gittest.domain.common.AppError

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T): NetworkResult<T>()
    data class Error(val error: AppError): NetworkResult<Nothing>()
}