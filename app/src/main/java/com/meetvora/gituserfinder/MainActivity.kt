package com.meetvora.gituserfinder

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.meetvora.gituserfinder.data.remote.ApiClient
import com.meetvora.gituserfinder.data.repository.GitHubRepository
import com.meetvora.gituserfinder.navigation.AppNavGraph
import com.meetvora.gituserfinder.ui.theme.GitUserFinderTheme
import com.meetvora.gituserfinder.viewmodel.SearchViewModel


/**
 * The main activity of the GitUserFinder app.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = SearchViewModel(GitHubRepository(ApiClient.api))

        setContent {
            GitUserFinderTheme {
                val navController = rememberNavController()
                AppNavGraph(navController, viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Preview() {
    val viewModel = SearchViewModel(GitHubRepository(ApiClient.api))

    GitUserFinderTheme {
        val navController = rememberNavController()
        AppNavGraph(navController, viewModel)
    }
}