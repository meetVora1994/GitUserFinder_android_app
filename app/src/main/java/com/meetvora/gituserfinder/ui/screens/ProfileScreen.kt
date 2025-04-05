package com.meetvora.gituserfinder.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.meetvora.gituserfinder.data.remote.ApiClient
import com.meetvora.gituserfinder.data.repository.GitHubRepository
import com.meetvora.gituserfinder.ui.components.TopBar
import com.meetvora.gituserfinder.ui.theme.GitUserFinderTheme
import com.meetvora.gituserfinder.viewmodel.ProfileViewModel


/**
 * Composable function for displaying the profile screen of a GitHub user.
 *
 * @param username The GitHub username of the user whose profile is to be displayed.
 * @param navController The NavController used for navigation within the app.
 * @param repo The GitHubRepository used for fetching user data.
 */
@Composable
fun ProfileScreen(
    username: String,
    navController: NavController?,
    repo: GitHubRepository
) {
    val viewModel = remember { ProfileViewModel(repo) }
    val user by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser(username)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = user?.login ?: "",
                onClickBack = {
                    navController?.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Surface(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            user?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .safeDrawingPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = it.avatarUrl,
                        contentDescription = "GitHub Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("@${it.login}", style = MaterialTheme.typography.titleMedium)
                    it.name?.let { name ->
                        Text(name, style = MaterialTheme.typography.titleLarge)
                    }
                    it.bio?.let { bio ->
                        Text(bio, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                navController?.navigate("followers/${it.login}")
                            },
                            enabled = it.followers > 0
                        ) {
                            Text(text = "${it.followers} Followers")
                        }
                        TextButton(
                            onClick = {
                                navController?.navigate("following/${it.login}")
                            },
                            enabled = it.following > 0
                        ) {
                            Text(text = "${it.following} Following")
                        }
                    }
                }
            } ?: run {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Preview() {
    GitUserFinderTheme {
        val username = "meetVora1994"
        ProfileScreen(username, null, GitHubRepository(ApiClient.api))
    }
}