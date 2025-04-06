package com.meetvora.gituserfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetvora.gituserfinder.data.model.GitHubUser
import com.meetvora.gituserfinder.data.repository.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


/**
 * UserListViewModel manages user data retrieved from the GitHubRepository.
 * It exposes a list of users as a StateFlow, which can be observed by the UI.
 *
 * @property repo The GitHubRepository instance used to fetch user data.
 */
class UserListViewModel(private val repo: GitHubRepository) : ViewModel() {
    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
        private set
    val users: MutableStateFlow<List<GitHubUser>?> = MutableStateFlow(null)

    fun getUserFollowerFollowingList(
        username: String,
        isFollowers: Boolean,
    ) {
        viewModelScope.launch {
            users.value = null
            uiState.value = UiState.Loading
            try {
                val response =
                    if (isFollowers) repo.getFollowers(username) else repo.getFollowing(username)
                if (response.isSuccessful) {
                    users.value = response.body()
                    uiState.value = UiState.Success
                } else {
                    if (response.code() == 404) {
                        uiState.value = UiState.Error("No users available.")
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