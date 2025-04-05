package com.meetvora.gituserfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.meetvora.gituserfinder.data.remote.ApiClient
import com.meetvora.gituserfinder.data.repository.GitHubRepository
import com.meetvora.gituserfinder.ui.screens.ProfileScreen
import com.meetvora.gituserfinder.ui.screens.SearchScreen
import com.meetvora.gituserfinder.ui.screens.UserListScreen
import com.meetvora.gituserfinder.viewmodel.SearchViewModel

/**
 *  Navigation graph for the application.
 */
@Composable
fun AppNavGraph(navController: NavHostController, viewModel: SearchViewModel) {
    NavHost(navController, startDestination = "search") {
        composable("search") {
            SearchScreen(viewModel = viewModel) { user ->
                navController.navigate("profile/${user.login}")
            }
        }
        composable("profile/{username}") {
            val username = it.arguments?.getString("username") ?: return@composable
            ProfileScreen(username, navController, GitHubRepository(ApiClient.api))
        }
        composable("followers/{username}") {
            val username = it.arguments?.getString("username") ?: return@composable
            UserListScreen(username, true, GitHubRepository(ApiClient.api), navController)
        }
        composable("following/{username}") {
            val username = it.arguments?.getString("username") ?: return@composable
            UserListScreen(username, false, GitHubRepository(ApiClient.api), navController)
        }

    }
}