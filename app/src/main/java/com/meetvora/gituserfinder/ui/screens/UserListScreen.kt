package com.meetvora.gituserfinder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.meetvora.gituserfinder.data.repository.GitHubRepository
import com.meetvora.gituserfinder.ui.components.TopBar
import com.meetvora.gituserfinder.viewmodel.UserListViewModel

/**
 * Composable function for displaying a list of users (followers or following).
 *
 * @param username The username of the user whose followers or following are to be displayed.
 * @param isFollowers Boolean flag indicating whether to display followers (true) or following (false).
 * @param repo The GitHubRepository instance for fetching data.
 * @param navController The NavController instance for navigation.
 */
@Composable
fun UserListScreen(
    username: String,
    isFollowers: Boolean,
    repo: GitHubRepository,
    navController: NavController
) {
    val viewModel = remember { UserListViewModel(repo) }
    val users by viewModel.users.collectAsState()

    LaunchedEffect(username) {
        viewModel.getUserFollowerFollowingList(username, isFollowers)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = if (isFollowers) "Followers" else "Following",
                onClickBack = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Surface(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            users?.let {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(it) { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("profile/${user.login}")
                                }
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = user.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(user.login, style = MaterialTheme.typography.titleMedium)
                                user.name?.let {
                                    Text(it, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            } ?: run {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
