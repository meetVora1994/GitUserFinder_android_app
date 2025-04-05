package com.meetvora.gituserfinder.data.repository

import com.meetvora.gituserfinder.data.model.GitHubUser
import com.meetvora.gituserfinder.data.remote.GitHubApi
import retrofit2.Response

/**
 * Repository class that acts as an intermediary between the ViewModel and the remote data source
 * (GitHub API) for user-related operations.
 */
class GitHubRepository(private val api: GitHubApi) {
    suspend fun getUser(username: String): Response<GitHubUser> = api.getUser(username)
    suspend fun getFollowers(username: String) = api.getFollowers(username)
    suspend fun getFollowing(username: String) = api.getFollowing(username)
}