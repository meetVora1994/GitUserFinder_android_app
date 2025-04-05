package com.meetvora.gituserfinder.data.remote

import com.meetvora.gituserfinder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * This object provides a lazy instance of the [GitHubApi] for making network requests.
 */
object ApiClient {
    val api: GitHubApi by lazy {
        val logging = HttpLoggingInterceptor()
        // We only want to log in DBEUG builds
        if (BuildConfig.DEBUG) logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }
}