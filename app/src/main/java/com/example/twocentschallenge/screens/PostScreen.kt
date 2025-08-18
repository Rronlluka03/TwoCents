package com.example.twocentschallenge.screens

import android.graphics.drawable.shapes.Shape
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.twocentschallenge.Models.Comment
import com.example.twocentschallenge.Models.CommentNode
import com.example.twocentschallenge.Models.PollOptions
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.Models.formatDateTime
import com.example.twocentschallenge.R
import com.example.twocentschallenge.enums.SubscriptionEnum
import com.example.twocentschallenge.ui.theme.AppTopBar
import com.example.twocentschallenge.ui.theme.CardBackground
import com.example.twocentschallenge.ui.theme.IconNetWorth
import com.example.twocentschallenge.ui.theme.Orange
import com.example.twocentschallenge.ui.theme.PostActions
import com.example.twocentschallenge.ui.theme.PostInfo
import com.example.twocentschallenge.ui.theme.UserInfo
import com.example.twocentschallenge.utils.PostUiState
import com.example.twocentschallenge.utils.formatCurrency
import com.example.twocentschallenge.viewModels.PostVM
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
    val pollstate = viewModel.poll.collectAsState().value
    val commentState = viewModel.commentsState.collectAsState().value

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

            when {
                uiState is PostUiState.Success<*> -> {
                    val post = uiState.post as Post

                    PostWithComments(post = post, pollstate, commentState) {
                        onPosterNetWorthClick(post.authorUuid)
                    }
                }

                uiState is PostUiState.Loading  -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState is PostUiState.Error -> {
                    Text(
                        "Error: ${(uiState as PostUiState.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    Text("Idle", color = Color.Gray)
                }
            }

        }

    }
}

@Composable
fun PostWithComments(post: Post, pollState: PostUiState<List<PollOptions>?>?, commentsState: PostUiState<List<CommentNode>?>, onNetworthclicked: () -> Unit) {
    val scrollState = rememberScrollState()
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Column(Modifier.padding(16.dp).verticalScroll(scrollState) , verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if(post.postType == 2) {
                        when(pollState) {
                            PostUiState.Loading -> CircularProgressIndicator()
                            is PostUiState.Error -> ErrorState(message = pollState.message)
                            is PostUiState.Success -> pollState.post?.let { PollResults(options = it) }
                            else -> Unit
                        }
                    }
            }
            PostInfo(post)
            PostActions(poster = post)

            if(post.commentCount > 0) {
                when(commentsState) {
                PostUiState.Loading -> CircularProgressIndicator()
                is PostUiState.Error -> ErrorState(message = commentsState.message)
                is PostUiState.Success -> commentsState.post?.let { commentNodes ->
                    // Use a regular Column instead of LazyColumn
                    Column {
                        commentNodes.forEach { node ->
                            CommentTree(node = node, onNetworthclicked)
                        }
                    }
                }
                else -> Unit
            }
            }
        }
    }
}

@Composable
fun PollResults(
    options: List<PollOptions>,
    modifier: Modifier = Modifier
) {
    val totalVotes = options.sumOf { it.votes }.coerceAtLeast(1)
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(
                width = 1.dp,
                color = CardBackground,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(10.dp)
    ) {
        options.forEach { opt ->
            val fraction by animateFloatAsState(
                targetValue = opt.votes.toFloat() / totalVotes,
                animationSpec = tween(durationMillis = 1800), label = ""
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
            ) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Orange)
                )
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = opt.question,
                        maxLines =  Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        style =  MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            opt.votes.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.width(8.dp))
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painterResource(R.drawable.usd_sign),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Orange
                                )
                                Text(
                                    formatCurrency(opt.averageBalance),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CommentTree(node: CommentNode, onNetworthclicked: () -> Unit) {
    Column(modifier = Modifier.padding(start = (node.depth * 16).dp)) {
        CommentItem(comment = node.comment, onNetworthclicked )

        node.children.forEach { childNode ->
            CommentTree(childNode,onNetworthclicked)
        }
    }
}

@Composable
fun CommentItem(comment: Comment, onNetworthclicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconNetWorth(
                    comment.authorMeta.subscriptionType,
                    amount = formatCurrency(comment.authorMeta.balance),
                    onClick = onNetworthclicked
                )
                Spacer(Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = comment.authorMeta.arena,
                        color = Color(0xFFAAAAAA),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = formatDateTime(comment.createdAt.toString()),
                        color = Color(0xFFAAAAAA),
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                text = comment.text,
                modifier = Modifier.padding(top = 8.dp),
                color = Color.White,
                fontSize = 14.sp
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(R.drawable.arrow_up),
                    contentDescription = "Upvotes",
                    tint = Color(0xFFAAAAAA),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = comment.upvoteCount.toString(),
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (comment.deletedAt != null) {
                Text(
                    text = "Comment deleted",
                    color = Color.Red,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(message, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
    }
}

