package com.example.twocentschallenge.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.R
import com.example.twocentschallenge.ui.theme.shimmer
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale





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
