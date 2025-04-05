package com.meetvora.gituserfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetvora.gituserfinder.data.model.GitHubUser
import com.meetvora.gituserfinder.data.repository.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel for managing and fetching GitHub user profiles.
 * @param repo The GitHub repository for data access.
 */
class ProfileViewModel(private val repo: GitHubRepository) : ViewModel() {
    var userState: MutableStateFlow<GitHubUser?> = MutableStateFlow(null)
        private set

    fun loadUser(username: String) {
        viewModelScope.launch {
            try {
                val res = repo.getUser(username)
                if (res.isSuccessful) {
                    userState.value = res.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}