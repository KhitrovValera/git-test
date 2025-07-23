package com.example.gittest.ui.common.mapper

import com.example.gittest.R
import com.example.gittest.domain.common.AppError
import com.example.gittest.ui.common.model.UiInfoState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorMapper @Inject constructor() {
    fun errorToUiState(error: AppError): UiInfoState {
        return when (error) {
            AppError.UnknownError -> UiInfoState(
                icon = R.drawable.ic_something_error,
                title = R.string.something_error,
                subtitle = R.string.something_error_description,
                buttonText = R.string.retry
            )
            else -> UiInfoState(
                icon = R.drawable.ic_network_error,
                title = R.string.connection_error,
                subtitle = R.string.connection_error_description,
                buttonText = R.string.retry
            )
        }
    }

    fun errorToResourceIds(error: AppError): Pair<Int, Int> {
        return when (error) {
            is AppError.UnknownError -> Pair(
                R.string.something_error,
                R.string.something_error_description
            )
            is AppError.ApiError -> when (error.code) {
                401 -> Pair(
                    R.string.auth_error,
                    R.string.auth_error_description
                )
                else -> Pair(
                    R.string.api_error,
                    R.string.api_error_description
                )
            }
            AppError.NetworkError -> Pair(
                R.string.connection_error,
                R.string.connection_error_description
            )
        }
    }
}