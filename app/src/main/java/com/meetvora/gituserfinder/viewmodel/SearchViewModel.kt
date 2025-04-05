package com.meetvora.gituserfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetvora.gituserfinder.data.model.GitHubUser
import com.meetvora.gituserfinder.data.repository.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel for searching GitHub users.
 */
class SearchViewModel(private val repo: GitHubRepository) : ViewModel() {
    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
        private set

    fun searchUser(username: String, onUserFound: (GitHubUser) -> Unit) {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            val response = repo.getUser(username)
            uiState.value = if (response.isSuccessful && response.body() != null) {
                onUserFound(response.body() ?: return@launch)
                UiState.Success
            } else {
                UiState.NotFound
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        object NotFound : UiState()
    }
}