package com.nawin.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.components.AnimatedCircularProgress
import com.nawin.habittracker.ui.theme.*
import com.nawin.habittracker.ui.viewmodel.HabitViewModel

@Composable
fun StatsScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()

    val totalTasks = habits.sumOf { it.subTasks.size }
    val completed = habits.sumOf { it.subTasks.count { it.isDone } }
    val totalHabits = habits.size
    val completedHabits = habits.count { hw ->
        hw.subTasks.isNotEmpty() && hw.subTasks.all { it.isDone }
    }
    val progress = if (totalTasks == 0) 0f else completed.toFloat() / totalTasks

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val motivMessage = when {
        progress == 0f  -> stringResource(R.string.motiv_0)
        progress < 0.3f -> stringResource(R.string.motiv_30)
        progress < 0.6f -> stringResource(R.string.motiv_60)
        progress < 1f   -> stringResource(R.string.motiv_90)
        else            -> stringResource(R.string.motiv_100)
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + slideInVertically(tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.stats_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BabyPinkLight, MatchaLight)
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedCircularProgress(progress)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    emoji = "✅",
                    label = stringResource(R.string.stat_tasks_done),
                    value = "$completed/$totalTasks",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    emoji = "🌿",
                    label = stringResource(R.string.stat_habits_done),
                    value = "$completedHabits/$totalHabits",
                    modifier = Modifier.weight(1f)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(BabyPink.copy(alpha = 0.3f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = motivMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MatchaDark,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StatCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BabyPinkLight),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(emoji, fontSize = 28.sp)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MatchaDark.copy(alpha = 0.6f)
            )
        }
    }
}