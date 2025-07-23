package com.example.gittest.data.mapper

import com.example.gittest.data.remote.result.NetworkResult
import com.example.gittest.domain.common.Resource

inline fun <T, R> NetworkResult<T>.toResource(
    crossinline mapper: (T) -> R
): Resource<R> {
    return when (this) {
        is NetworkResult.Success -> Resource.Success(mapper(data))
        is NetworkResult.Error -> Resource.Error(error)
    }
}