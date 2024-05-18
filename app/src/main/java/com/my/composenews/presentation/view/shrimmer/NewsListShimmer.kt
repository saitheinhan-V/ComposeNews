package com.my.composenews.presentation.view.shrimmer

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun NewsListShimmer() {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    )
    val transition = rememberInfiniteTransition(label = "")
    val anim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        start = Offset.Zero,
        end = Offset(x = anim.value, y = anim.value),
        colors = shimmerColors
    )

    Column {
        repeat(2) {
            NewsListShimmerItem(
                brush = brush,
                fraction = 0.5f
            )
        }
        repeat(2){
            NewsListShimmerItem(
                brush = brush,
                fraction = 0.3f
            )
        }
    }
}

@Composable
fun NewsListShimmerItem(
    modifier: Modifier = Modifier,
    brush: Brush,
    fraction: Float = Random.nextFloat().coerceIn(
        minimumValue = 0.5f,
        maximumValue = 9.0f
    )
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush = brush)
        )
        Spacer(
            modifier = modifier.height(8.dp)
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth(fraction = fraction)
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush = brush)
        )
    }
}

@Preview
@Composable
fun PreviewShimmerItem() {
    Surface {
        NewsListShimmerItem(
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
            )
        )
    }
}

@Preview
@Composable
fun PreviewShimmerList(){
    Surface {
        NewsListShimmer()
    }
}