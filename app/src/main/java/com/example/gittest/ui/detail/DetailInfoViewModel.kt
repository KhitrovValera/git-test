package com.example.gittest.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gittest.domain.common.AppError
import com.example.gittest.domain.common.Resource
import com.example.gittest.domain.model.RepoDetails
import com.example.gittest.domain.useCase.GetRepoPageUseCase
import com.example.gittest.ui.common.mapper.ErrorMapper
import com.example.gittest.ui.common.model.UiInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailInfoViewModel @Inject constructor(
    private val getRepoPageUseCase: GetRepoPageUseCase,
    private val errorMapper: ErrorMapper,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val repoId: String? = savedStateHandle.get<String>("repoId")

    init {
        retryLoadData()
    }

    fun retryLoadData() {
        if (repoId != null) {
            _state.value = State.Loading
            loadDetails(repoId)
        } else {
            _state.value = State.Error(errorMapper.errorToUiState(AppError.UnknownError))
        }
    }

    fun loadDetails(repoId: String) {
        viewModelScope.launch {
            val result = getRepoPageUseCase.getRepoDetailsUseCase(repoId)
            when (result) {
                is Resource.Error -> {
                    _state.value = State.Error(errorMapper.errorToUiState(result.error))
                }
                is Resource.Success -> {
                    val repoDetails = result.data
                    _state.value = State.Loaded(repoDetails, ReadmeState.Loading)

                    val readMe = getRepoPageUseCase.getReadMeUseCase(
                        repoDetails.ownerLogin,
                        repoDetails.name,
                        repoDetails.defaultBranch
                    )
                    when (readMe) {
                        is Resource.Error -> {
                            _state.value = State.Loaded(repoDetails, ReadmeState.Error(
                                errorMapper.errorToUiState(readMe.error)
                            ))
                        }
                        is Resource.Success -> {
                            _state.value = if (!readMe.data.isEmpty()) State.Loaded(repoDetails, ReadmeState.Loaded(readMe.data))
                            else State.Loaded(repoDetails, ReadmeState.Empty)
                        }
                    }
                }
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: UiInfoState) : State
        data class Loaded(
            val githubRepo: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: UiInfoState) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

}