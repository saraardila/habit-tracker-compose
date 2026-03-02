package com.nawin.habittracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*

@Composable
fun AnimatedCircularProgress(progress: Float) {

    val animated by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800, easing = EaseInOutCubic),
        label = "circularProgress"
    )

    // Rotación infinita suave cuando no está completo
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {

            Canvas(modifier = Modifier.size(180.dp)) {

                // Track
                drawCircle(
                    color = MatchaLight,
                    radius = size.minDimension / 2,
                    style = Stroke(width = 28f)
                )

                // Progreso con gradiente simulado
                drawArc(
                    color = if (progress == 1f) Matcha
                    else BabyPink.copy(alpha = glowAlpha),
                    startAngle = -90f,
                    sweepAngle = 360f * animated,
                    useCenter = false,
                    style = Stroke(width = 28f, cap = StrokeCap.Round)
                )

                // Segundo arco encima para efecto degradado
                if (animated > 0.1f) {
                    drawArc(
                        color = Matcha.copy(alpha = 0.6f),
                        startAngle = -90f,
                        sweepAngle = 360f * animated * 0.5f,
                        useCenter = false,
                        style = Stroke(width = 28f, cap = StrokeCap.Round)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(animated * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MatchaDark
                )
                Text(
                    text = stringResource(
                        R.string.progress_label,
                        (progress * 100).toInt()
                    ),                    style = MaterialTheme.typography.labelSmall,
                    color = MatchaDark.copy(alpha = 0.6f)
                )
            }
        }

        // Badge si completó todo
        if (progress == 1f) {
            Text(
                text = stringResource(R.string.badge_perfect_day),
                style = MaterialTheme.typography.labelLarge,
                color = MatchaDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}