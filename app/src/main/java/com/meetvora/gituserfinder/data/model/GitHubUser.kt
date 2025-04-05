package com.meetvora.gituserfinder.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing a GitHub user's profile.
 *
 * This class is designed to hold information about a user fetched from the GitHub API.
 * It uses Moshi annotations for efficient JSON serialization and deserialization.
 */
@JsonClass(generateAdapter = true)
data class GitHubUser(
    val login: String,
    val name: String?,
    @Json(name = "avatar_url") val avatarUrl: String,
    val bio: String?,
    val followers: Int = 0,
    val following: Int = 0
)