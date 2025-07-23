package com.example.gittest.ui.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gittest.data.local.KeyValueStorage
import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.model.Repo
import com.example.gittest.domain.useCase.GetReposUseCase
import com.example.gittest.ui.common.mapper.ErrorMapper
import com.example.gittest.ui.common.model.UiInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val getReposUseCase: GetReposUseCase,
    private val keyValueStorage: KeyValueStorage,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _actions = MutableSharedFlow<Action>()
    val actions: SharedFlow<Action> = _actions

    init {
        loadRepo()
    }

    fun loadRepo() {
        if (state.value is State.Content) return
        viewModelScope.launch {
            _state.value = State.Loading

            val result = getReposUseCase()
            when (result) {
                is Resource.Success -> {
                    val repos = result.data
                    _state.value = if (repos.isEmpty()) State.Empty
                    else State.Content(repos)
                }
                is Resource.Error -> {
                    _state.value = State.Error(errorMapper.errorToUiState(result.error))
                }
            }
        }
    }

    fun onRepoPressed(repoId: String) {
        viewModelScope.launch {
            _actions.emit(Action.RouteToRepo(repoId))
        }
    }

    fun onExitButtonPressed() {
        viewModelScope.launch {
            _actions.emit(Action.ExitAccount)
            keyValueStorage.authToken = null
        }
    }

    sealed interface State {
        object Loading : State
        data class Content(val repos: List<Repo>) : State
        object Empty : State
        data class Error(
            val message: UiInfoState
        ) : State
    }

    sealed interface Action {
        object ExitAccount : Action
        data class RouteToRepo(val repoId: String) : Action
    }
}