package com.nawin.habittracker.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*
import com.nawin.habittracker.ui.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val today = LocalDate.now()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + slideInVertically(tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.nav_calendar),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )

            // Card calendario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BabyPinkLight, MatchaLight)
                        )
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header mes con flechas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Icon(Icons.Default.ChevronLeft, null, tint = MatchaDark)
                    }
                    Text(
                        text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                            .replaceFirstChar { it.uppercase() } + " ${currentMonth.year}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MatchaDark
                    )
                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Icon(Icons.Default.ChevronRight, null, tint = MatchaDark)
                    }
                }

                // Días de la semana
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            color = MatchaDark.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Grid días
                val firstDay = currentMonth.atDay(1)
                val daysInMonth = currentMonth.lengthOfMonth()
                val startOffset = (firstDay.dayOfWeek.value - 1)
                val totalCells = startOffset + daysInMonth
                val rows = (totalCells + 6) / 7

                repeat(rows) { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        repeat(7) { col ->
                            val dayIndex = row * 7 + col - startOffset + 1
                            if (dayIndex < 1 || dayIndex > daysInMonth) {
                                Spacer(modifier = Modifier.weight(1f))
                            } else {
                                val date = currentMonth.atDay(dayIndex)
                                val isToday = date == today
                                val isPast = date.isBefore(today)

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isToday -> Matcha
                                                isPast  -> BabyPink.copy(alpha = 0.4f)
                                                else    -> androidx.compose.ui.graphics.Color.Transparent
                                            }
                                        )
                                        .then(
                                            if (isToday) Modifier.border(2.dp, MatchaDark, CircleShape)
                                            else Modifier
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isPast && !isToday) "✓" else dayIndex.toString(),
                                        fontSize = 11.sp,
                                        color = if (isToday) CreamWhite else MatchaDark,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Leyenda
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                LegendItem(color = Matcha, label = stringResource(R.string.calendar_today))
                LegendItem(color = BabyPink.copy(alpha = 0.4f), label = stringResource(R.string.calendar_completed))
            }
        }
    }
}

@Composable
fun LegendItem(
    color: androidx.compose.ui.graphics.Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MatchaDark.copy(alpha = 0.7f)
        )
    }
}