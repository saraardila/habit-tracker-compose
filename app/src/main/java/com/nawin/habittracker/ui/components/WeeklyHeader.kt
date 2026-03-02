package com.nawin.habittracker.ui.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyHeader(
    progress: Float,
    currentStreak: Int,
    completedDays: Map<String, Float> // fecha -> completion rate
) {
    val today = LocalDate.now()

    // Bounce osito
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
        progress == 0f  -> "🐻"
        progress < 0.3f -> "🐻"
        progress < 0.6f -> "🐻‍❄️"
        progress < 1f   -> "✨🐻✨"
        else            -> "🎉🐻🎉"
    }

    val message = when {
        progress == 0f  -> stringResource(R.string.bear_0)
        progress < 0.3f -> stringResource(R.string.bear_30)
        progress < 0.6f -> stringResource(R.string.bear_60)
        progress < 1f   -> stringResource(R.string.bear_90)
        else            -> stringResource(R.string.bear_100)
    }

    // Frase motivacional del día según hora
    val hour = remember { today.atStartOfDay().hour }
    val greeting = when {
        hour < 12 -> stringResource(R.string.greeting_morning)
        hour < 18 -> stringResource(R.string.greeting_afternoon)
        else      -> stringResource(R.string.greeting_evening)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(BabyPinkLight, MatchaLight)
                )
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Fecha + saludo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.labelMedium,
                    color = MatchaDark.copy(alpha = 0.7f)
                )
                Text(
                    text = today.format(
                        java.time.format.DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MatchaDark
                )
            }

            // Streak badge
            if (currentStreak > 0) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(BabyPink.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("🔥", fontSize = 16.sp)
                    Text(
                        text = "$currentStreak ${stringResource(R.string.streak_days)}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MatchaDark
                    )
                }
            }
        }

        // Osito animado + mensaje
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = bear,
                fontSize = 48.sp,
                modifier = Modifier.scale(scale)
            )
            Column {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MatchaDark,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${(progress * 100).toInt()}% ${stringResource(R.string.progress_label)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MatchaDark.copy(alpha = 0.6f)
                )
            }
        }

        // Barra de progreso
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(CreamWhite.copy(alpha = 0.6f))
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

        // Calendario semanal
        WeeklyCalendar(
            today = today,
            completedDays = completedDays
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyCalendar(
    today: LocalDate,
    completedDays: Map<String, Float>
) {
    val weekDays = (-3..3).map { today.plusDays(it.toLong()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        weekDays.forEach { day ->
            val dateKey = day.toString()
            val isToday = day == today
            val completionRate = completedDays[dateKey] ?: 0f
            val isDone = completionRate == 1f
            val isPast = day.isBefore(today)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Nombre del día
                Text(
                    text = day.dayOfWeek
                        .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        .take(1),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isToday) MatchaDark else MatchaDark.copy(alpha = 0.5f),
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                )

                // Círculo del día
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isToday && isDone -> Matcha
                                isToday          -> BabyPink
                                isDone           -> MatchaLight
                                isPast           -> CreamWhite.copy(alpha = 0.5f)
                                else             -> CreamWhite.copy(alpha = 0.3f)
                            }
                        )
                        .then(
                            if (isToday) Modifier.border(2.dp, MatchaDark, CircleShape)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Text("✓", fontSize = 14.sp, color = MatchaDark, fontWeight = FontWeight.Bold)
                    } else {
                        Text(
                            text = day.dayOfMonth.toString(),
                            fontSize = 12.sp,
                            color = if (isToday) MatchaDark else MatchaDark.copy(alpha = 0.5f),
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}