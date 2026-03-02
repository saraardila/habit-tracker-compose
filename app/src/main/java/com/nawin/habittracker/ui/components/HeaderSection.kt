package com.nawin.habittracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*

@Composable
fun HeaderSection(progress: Float) {

    // Bounce del osito
    val infiniteTransition = rememberInfiniteTransition(label = "bear")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    val bear = when {
        progress == 0f   -> "🐻"
        progress < 0.3f  -> "🐻"
        progress < 0.6f  -> "🐻‍❄️"
        progress < 1f    -> "✨🐻✨"
        else             -> "🎉🐻🎉"
    }

    val message = when {
        progress == 0f   -> stringResource(R.string.bear_0)
        progress < 0.3f  -> stringResource(R.string.bear_30)
        progress < 0.6f  -> stringResource(R.string.bear_60)
        progress < 1f    -> stringResource(R.string.bear_90)
        else             -> stringResource(R.string.bear_100)
    }

    // Lottie sparkles cuando completa todo
    val showSparkles = progress == 1f
    val sparklesComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.celebration)
    )
    val sparklesProgress by animateLottieCompositionAsState(
        composition = sparklesComposition,
        isPlaying = showSparkles,
        restartOnPlay = true
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(BabyPinkLight, MatchaLight)
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        // Lottie sparkles de fondo cuando 100%
        if (showSparkles) {
            LottieAnimation(
                composition = sparklesComposition,
                progress = { sparklesProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = bear,
                fontSize = 64.sp,
                modifier = Modifier.scale(scale)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MatchaDark,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(16.dp))

            // Barra de progreso
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White.copy(alpha = 0.6f))
            ) {
                val animatedProgress by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = tween(600),
                    label = "progress"
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(BabyPink, Matcha)
                            )
                        )
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "${(progress * 100).toInt()}% completed today",
                style = MaterialTheme.typography.labelSmall,
                color = MatchaDark.copy(alpha = 0.7f)
            )
        }
    }
}