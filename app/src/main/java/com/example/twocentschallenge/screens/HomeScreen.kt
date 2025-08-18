package com.example.twocentschallenge.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.twocentschallenge.viewModels.HomeVM
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.utils.PostUiState
import com.example.twocentschallenge.R
import com.example.twocentschallenge.ui.theme.AppTopBar
import com.example.twocentschallenge.ui.theme.IconNetWorth
import com.example.twocentschallenge.ui.theme.PostActions
import com.example.twocentschallenge.ui.theme.shimmer
import com.example.twocentschallenge.utils.formatCurrency
import com.example.twocentschallenge.utils.subscriptionColor
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(viewModel: HomeVM = hiltViewModel(), onPostClick: (String) -> Unit,
               onPosterNetWorthClick: (String) -> Unit) {
    val uiState = viewModel.uiState.collectAsState().value
    Scaffold(
        topBar = {
            AppTopBar(
                "TwoCents",false
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            when (uiState) {
                is PostUiState.Idle,
                PostUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is PostUiState.Success<*> -> {
                    val posts = (uiState).post as List<Post>
                    LazyColumn {
                        items(posts.size ?: 0) { post ->
                            PostItems(
                                posts.get(post),
                                onClick = { posts.get(post).let { onPostClick(it.uuid) } },
                                onNetworthclicked = { onPosterNetWorthClick(posts.get(post).authorUuid) }
                            )

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
                    )
                }
            }
        }
    }

}

@Composable
fun PostItems(
    item: Post,
    onClick: () -> Unit,
    onNetworthclicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
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

                IconNetWorth(
                    item.authorMetaDto.subscriptionType,
                    amount = formatCurrency(item.authorMetaDto.balance),
                    onClick = onNetworthclicked
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(item.text, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Spacer(modifier = Modifier.width(5.dp))
            PostActions(poster = item)
        }
    }
}

@Composable
fun UserAssets(
    number: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = number.toString(),
        style = MaterialTheme.typography.labelSmall,
        color = Color.White,
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

