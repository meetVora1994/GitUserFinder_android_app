package com.meetvora.gituserfinder.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.meetvora.gituserfinder.R
import com.meetvora.gituserfinder.data.remote.ApiClient
import com.meetvora.gituserfinder.data.repository.GitHubRepository
import com.meetvora.gituserfinder.ui.components.TopBar
import com.meetvora.gituserfinder.ui.theme.GitUserFinderTheme
import com.meetvora.gituserfinder.viewmodel.SearchViewModel


/**
 * Composable function for the search screen.
 *
 * @param viewModel The [SearchViewModel] instance.
 * @param navController The NavController used for navigation within the app.
 */
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController?) {
    var query by remember { mutableStateOf("") }
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = stringResource(R.string.app_name),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("GitHub Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.searchUser(
                        username = query,
                        onUserFound = { user ->
                            navController?.navigate("profile/${user.login}")
                        }
                    )
                },
                enabled = query.isNotEmpty(),
            ) {
                Text("Search")
            }

            Spacer(Modifier.height(16.dp))

            when (state) {
                is SearchViewModel.UiState.Loading -> CircularProgressIndicator()
                is SearchViewModel.UiState.NotFound -> Text("User not found.")
                else -> {}
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
        SearchScreen(viewModel = viewModel, navController = null)
    }
}