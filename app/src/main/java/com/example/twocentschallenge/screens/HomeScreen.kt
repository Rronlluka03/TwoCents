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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults.contentColor
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
import com.example.twocentschallenge.utils.PostActions
import com.example.twocentschallenge.utils.PostUiState
import com.example.twocentschallenge.R
import com.example.twocentschallenge.ui.theme.AppTopBar
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
        containerColor = Color.Black // full screen gray
    ) {

        when (uiState) {
            is PostUiState.Idle,
            PostUiState.Loading -> {
            }

            is PostUiState.Success<*> -> {
                val posts = (uiState).post as List<Post>
                LazyColumn(contentPadding = it) {
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
                // Show error feedback
                val message = (uiState as PostUiState.Error).message
                Text(
                    "Error: $message",
                    color = MaterialTheme.colorScheme.error,
                )
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
                    color = contentColor,
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
            Spacer(modifier = Modifier.width(5.dp))
            PostActions(poster = item)
        }
    }
}

@Composable
fun IconNetWorth(
    subscriptionType: String,
    amount: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(32.dp),
    borderWidth: Dp = 1.dp,
    padding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    onClick: () -> Unit = {}
) {
    val color = subscriptionColor(subscriptionType)
    Row(
        modifier = modifier
            .clip(shape)
            .background(color.first)
            .border(borderWidth, color.second, shape)
            .clickable(onClick = onClick)
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),

    ) {

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color.first)
                .border(
                    width = 2.dp,
                    color = color.second,
                    shape = CircleShape
                )
                .shimmer(durationMillis = 1000),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(id = R.drawable.usd_sign),
                contentDescription = null,
                tint = color.second,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = amount,
            style = MaterialTheme.typography.titleSmall,
            color = color.second
        )
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

fun formatCurrency(amount: BigDecimal): String {
    val fmt = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return fmt.format(amount)
}

fun subscriptionColor(subscription: String) : Pair<Color,Color> {
    return when(subscription) {
        "0" ->  Pair(Color.Black, Color.White)
        "1" ->  Pair(Color(0xFFCD7F32), Color.Black)
        "2" ->  Pair(Color(0xFFC0C0C0), Color.Black)
        "3" ->  Pair(Color(0xFFFFD700), Color.Black)
        "4" ->  Pair(Color(0xFF2D2C28), Color.White)
        else -> Pair(Color.White, Color.Black)
    }
}

fun Modifier.shimmer(
    colors: List<Color> = listOf(Color.Transparent, Color.White.copy(alpha = 0.4f), Color.Transparent),
    shimmerWidth: Float = 200f,
    durationMillis: Int = 1200
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "")
    val progress by transition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    drawWithCache {
        val widthPx   = size.width
        val band      = shimmerWidth
        val offsetX   = (widthPx + band) * progress - band
        val brush     = Brush.linearGradient(
            colors,
            start = Offset(offsetX, 0f),
            end   = Offset(offsetX + band, 0f)
        )

        onDrawWithContent {
            drawContent()
            drawRect(brush, blendMode = BlendMode.SrcOver)
        }
    }
}