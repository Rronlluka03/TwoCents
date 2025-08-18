package com.example.twocentschallenge.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.twocentschallenge.viewModels.AuthorVM
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.R
import com.example.twocentschallenge.ui.theme.AppTopBar
import com.example.twocentschallenge.ui.theme.ComposableLifecycle
import com.example.twocentschallenge.utils.PostUiState

@Composable
fun AuthorPostsScreen(navController: NavController, authorId: String, viewModel: AuthorVM = hiltViewModel(), onPostClick: (String) -> Unit) {
    ListenForScreenLifecycle(viewModel, authorId)

    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            AppTopBar("TwoCents", true, onBackClick = {
                navController.popBackStack()
            })
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            when (uiState) {
                is PostUiState.Idle,
                PostUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is PostUiState.Success<*> -> {
                    val posts = (uiState).post as List<Post?>
                    LazyColumn {
                        items(posts.size ?: 0) { post ->
                            var item = posts.get(post)
                            if (item != null) {
                                PostItem(
                                    item, onClick = { onPostClick(item.uuid) }
                                )
                            }

                            Divider(
                                color = Color.Gray,
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                is PostUiState.Error -> {
                    val message = (uiState as PostUiState.Error).message
                    Text(
                        "Error: $message",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun PostItem(
    item: Post,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp) .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Column(Modifier.padding(16.dp) , verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text =  if(item.title.isNotEmpty()) item.title else  item.topic,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(item.text, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Spacer(modifier = Modifier.width(5.dp))
            PostButtons(poster = item)
        }
    }
}

@Composable
fun PostButtons(
    poster: Post,
    modifier: Modifier = Modifier,
    arrowUpClicked:() -> Unit = {},
    thumbsUp:() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.clickable( onClick = arrowUpClicked)) { ItemInfo(icon = R.drawable.arrow_up, text = "${poster.upvoteCount}") }
        Box(modifier = Modifier.clickable( onClick = thumbsUp )) { ItemInfo(icon = R.drawable.thumbs_up, text = "${poster.commentCount}") }
        ItemInfo(icon = R.drawable.eye, text = "${poster.viewCount}")
    }
}

@Composable
private fun ItemInfo(
    icon: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

@Composable
fun ListenForScreenLifecycle(viewModel: AuthorVM, authorId: String) {
    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                viewModel.getPostById(authorId)
            }

            else -> {
                // nothing
            }
        }
    }
}