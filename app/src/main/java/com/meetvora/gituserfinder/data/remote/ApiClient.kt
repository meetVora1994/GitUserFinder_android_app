package com.meetvora.gituserfinder.data.remote

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.meetvora.gituserfinder.App.Companion.applicationContext
import com.meetvora.gituserfinder.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * This object provides a lazy instance of the [GitHubApi] for making network requests.
 */
object ApiClient {
    private val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
    private val cache = Cache(applicationContext.cacheDir, cacheSize)

    val api: GitHubApi by lazy {
        val logging = HttpLoggingInterceptor()
        // We only want to log in DBEUG builds
        if (BuildConfig.DEBUG) logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(applicationContext)) {
                    request.newBuilder()
                        // cache is valid for 60 seconds if online
                        .header("Cache-Control", "public, max-age=" + 60)
                        .build()
                } else {
                    request.newBuilder()
                        // means it can use stale data for a day if offline
                        .header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24
                        )
                        .build()
                }
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
private fun hasNetwork(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}
