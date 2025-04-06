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
            try {
                val response = repo.getUser(username)
                if (response.isSuccessful) {
                    onUserFound(response.body() ?: return@launch)
                    uiState.value = UiState.Success
                } else {
                    if (response.code() == 404) {
                        uiState.value =
                            UiState.Error("User not found.\nPlease make sure you typed the username correctly.")
                    } else if (response.code() == 504) {
                        uiState.value = UiState.NetworkError
                    } else {
                        uiState.value =
                            UiState.Error("Something went wrong.\nPlease try again later.")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.value = UiState.Error("Something went wrong.\nPlease try again later.")
            }
        }
    }

}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    object Success : UiState()
    object NetworkError : UiState()
    data class Error(val description: String) : UiState()
}