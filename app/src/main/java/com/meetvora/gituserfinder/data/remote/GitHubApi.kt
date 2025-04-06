package com.meetvora.gituserfinder.data.remote

import com.meetvora.gituserfinder.data.model.GitHubUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Retrofit API service for interacting with the GitHub API.
 */
interface GitHubApi {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<GitHubUser>

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Response<List<GitHubUser>>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Response<List<GitHubUser>>
}