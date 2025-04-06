package com.meetvora.gituserfinder.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meetvora.gituserfinder.R


/**
 * A composable function that displays a network error message with an icon.
 *
 * @param modifier The modifier to apply to the layout.
 */
@Composable
fun NetworkErrorView(modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically),
    ) {
        androidx.compose.material3.Icon(
            painter = painterResource(id = R.drawable.baseline_cloud_off_24),
            contentDescription = "Network Error",
            modifier = Modifier.size(60.dp),
        )
        Text(
            text = "Network Error\nPlease check your internet connection.",
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun Preview() {
    NetworkErrorView()
}