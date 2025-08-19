package com.example.twocentschallenge.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.twocentschallenge.Models.Author
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.Models.formatDateTime
import com.example.twocentschallenge.R
import com.example.twocentschallenge.utils.subscriptionColor
import java.time.Instant


@Composable
fun IconNetWorth(
    subscriptionType: String,
    amount: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
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
            .padding(padding).shimmer(durationMillis = 1000),

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
                ),
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
fun PostInfo(post: Post) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemInfo(icon = R.drawable.star, text = post.authorMetaDto.age.toString())
        ItemInfo(icon = R.drawable.thumbs_up, text = post.authorMetaDto.gender)
        ItemInfo(icon = R.drawable.location, text = post.authorMetaDto.arena)
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
fun PostActions(
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
        Box(modifier = Modifier.clickable( onClick = thumbsUp )) { ItemInfo(icon = R.drawable.comment, text = "${poster.commentCount}") }
        ItemInfo(icon = R.drawable.eye, text = "${poster.viewCount}")
    }
}


@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun UserInfo(
    poster: Author,
    postedAt: String? = null,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ItemInfo(icon = R.drawable.location, text = poster.arena)
        if (postedAt != null) Text(
            text = formatDateTime(postedAt),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )

    }
}