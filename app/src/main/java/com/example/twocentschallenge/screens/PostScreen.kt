package com.example.twocentschallenge.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.ui.theme.AppTopBar
import com.example.twocentschallenge.utils.PostActions
import com.example.twocentschallenge.utils.PostInfo
import com.example.twocentschallenge.utils.PostUiState
import com.example.twocentschallenge.viewModels.PostVM

@Composable
fun PostScreen(
    navController: NavController,
    postId: String,
    viewModel: PostVM = hiltViewModel(), onPostClick: (String) -> Unit,
    onPosterNetWorthClick: (String) -> Unit
) {
    LaunchedEffect(postId) {
        viewModel.getPostById(postId)
    }

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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(16.dp)) {
            when (uiState) {
                is PostUiState.Idle,
                PostUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is PostUiState.Success<*> -> {
                    val post = (uiState).post as Post
                    PostWithComments(post,
                        onNetworthclicked = { onPosterNetWorthClick(post.authorUuid) })
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
fun PostWithComments(post: Post, onNetworthclicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = 8.dp),
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
                    text =  if(post.title.isNotEmpty()) post.title else  post.topic,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                IconNetWorth(
                    post.authorMetaDto.subscriptionType,
                    amount = formatCurrency(post.authorMetaDto.balance),
                    onClick = onNetworthclicked
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(post.text, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Spacer(modifier = Modifier.width(5.dp))
            PostInfo(post)
            PostActions(poster = post)
        }
    }
}
