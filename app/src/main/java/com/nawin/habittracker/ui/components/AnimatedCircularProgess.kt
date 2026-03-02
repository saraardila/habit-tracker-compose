package com.nawin.habittracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCircularProgress(progress: Float) {

    val animated by animateFloatAsState(targetValue = progress)

    val colors = MaterialTheme.colorScheme   // ← AQUÍ

    Box(contentAlignment = Alignment.Center) {

        Canvas(modifier = Modifier.size(180.dp)) {

            drawCircle(
                color = colors.surfaceVariant,
                radius = size.minDimension / 2,
                style = Stroke(width = 24f)
            )

            drawArc(
                color = colors.primary,
                startAngle = -90f,
                sweepAngle = 360f * animated,
                useCenter = false,
                style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
        }

        Text(
            "${(animated * 100).toInt()}%",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}