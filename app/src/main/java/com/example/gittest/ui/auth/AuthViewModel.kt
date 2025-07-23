package com.example.gittest.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gittest.R
import com.example.gittest.data.local.KeyValueStorage
import com.example.gittest.domain.common.AppError
import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.model.UserInfo
import com.example.gittest.domain.useCase.SignInUseCase
import com.example.gittest.ui.common.mapper.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val keyValueStorage: KeyValueStorage,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    private val _actions = MutableSharedFlow<Action>()
    val actions: SharedFlow<Action> = _actions

    private val _userInfo = MutableStateFlow<UserInfo>(UserInfo())
    val userInfo: StateFlow<UserInfo> = _userInfo

    init {
        val lastToken = keyValueStorage.authToken
        if (lastToken != null) {
            updateToken(lastToken)
            onSignButtonPressed()
        }
    }

    fun updateToken(newValue: String) {
        _token.value = newValue
    }

    fun setStateActive() {
        _state.value = State.Active
    }

    fun onSignButtonPressed() {
        val validatedToken = getValidTokenOrNull(token.value)

        if (validatedToken != null) {
            executeSignIn(validatedToken)
        }
    }

    private fun getValidTokenOrNull(token: String?): String? {
        val tokenPattern = TOKEN_REGEX
        if (token.isNullOrBlank() || !token.matches(tokenPattern)) {
            _state.value = State.InvalidInput(R.string.invalid_token.toString())
            viewModelScope.launch {
                _actions.emit(Action.ShowError(errorMapper.errorToResourceIds(AppError.ApiError(401))))
            }
            return null
        }
        return token
    }

    private fun executeSignIn(token: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            keyValueStorage.authToken = token

            when (val result = signInUseCase()) {
                is Resource.Success -> {
                    _userInfo.value = result.data
                    _actions.emit(Action.RouteToMain)
                }
                is Resource.Error -> {
                    _actions.emit(Action.ShowError(errorMapper.errorToResourceIds(result.error)))
                    _state.value = State.InvalidInput(R.string.invalid_token.toString())
                    keyValueStorage.authToken = null
                }
            }
        }
    }

    companion object {
        private val TOKEN_REGEX = "^[a-zA-Z0-9_]+$".toRegex()
    }

    sealed interface State {
        object Idle : State
        object Active : State
        object Loading: State
        data class InvalidInput(val reason: String) : State
    }

    sealed interface Action {
        data class ShowError(val message: Pair<Int, Int>) : Action
        object RouteToMain : Action
    }
}