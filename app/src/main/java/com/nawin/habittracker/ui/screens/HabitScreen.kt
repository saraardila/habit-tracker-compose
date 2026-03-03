package com.nawin.habittracker.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nawin.habittracker.R
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.ui.components.*
import com.nawin.habittracker.ui.theme.*
import com.nawin.habittracker.ui.viewmodel.HabitViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    navController: NavController? = null
) {
    val habits by viewModel.habits.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            // FAB solo aparece si hay hábitos
            AnimatedVisibility(
                visible = habits.isNotEmpty(),
                enter = scaleIn(tween(200)) + fadeIn(tween(200)),
                exit = scaleOut(tween(200)) + fadeOut(tween(200))
            ) {
                ExtendedFloatingActionButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = Matcha,
                    contentColor = CreamWhite,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = {
                        Text(
                            text = stringResource(R.string.fab_add_habit),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {

            // Header con mascota
            item {
                WeeklyHeader(
                    progress = calculateDailyProgress(habits),
                    currentStreak = habits.maxOfOrNull { it.habit.currentStreak } ?: 0,
                    completedDays = emptyMap()
                )
            }

            // Accesos rápidos
            item {
                QuickAccessRow(navController = navController)
            }

            // Lista hábitos o empty state
            if (habits.isEmpty()) {
                item {
                    EmptyStateInline { showDialog = true }
                }
            } else {
                items(habits, key = { it.habit.id }) { habitWithSubTasks ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteHabit(habitWithSubTasks.habit)
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFA5A5))
                                    .padding(end = 24.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        modifier = Modifier.clip(RoundedCornerShape(16.dp))
                    ) {
                        HabitCard(
                            habitWithSubTasks = habitWithSubTasks,
                            onToggle = viewModel::toggleSubTask,
                            onRenameHabit = { viewModel.updateHabitTitle(habitWithSubTasks.habit, it) },
                            onRenameSubTask = { subTask, newTitle -> viewModel.renameSubTask(subTask, newTitle) },
                            onDelete = { viewModel.deleteHabit(habitWithSubTasks.habit) }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            CreateHabitDialog(
                onDismiss = { showDialog = false },
                onCreate = { title, subtasks ->
                    viewModel.createHabit(title, subtasks)
                    showDialog = false
                }
            )
        }
    }
}

// Accesos rápidos a Calendario, Stats y Diario
@Composable
fun QuickAccessRow(navController: NavController?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickAccessCard(
            icon = Icons.Default.CalendarMonth,
            label = stringResource(R.string.nav_calendar),
            iconColor = Matcha,
            bgColor = MatchaLight,
            modifier = Modifier.weight(1f),
            onClick = { navController?.navigate("calendar") }
        )
        QuickAccessCard(
            icon = Icons.Default.BarChart,
            label = stringResource(R.string.nav_stats),
            iconColor = BabyPinkDark,
            bgColor = BabyPinkLight,
            modifier = Modifier.weight(1f),
            onClick = { navController?.navigate("stats") }
        )
        QuickAccessCard(
            icon = Icons.Default.AutoStories,
            label = stringResource(R.string.nav_diary),
            iconColor = Color(0xFFB39DDB),
            bgColor = Color(0xFFF3E5F5),
            modifier = Modifier.weight(1f),
            onClick = { navController?.navigate("diary") }
        )
    }
}

@Composable
fun QuickAccessCard(
    icon: ImageVector,
    label: String,
    iconColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MatchaDark,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Empty state inline dentro del LazyColumn
@Composable
fun EmptyStateInline(onAdd: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🌱", fontSize = 56.sp)
        Text(
            text = stringResource(R.string.empty_state),
            style = MaterialTheme.typography.titleMedium,
            color = MatchaDark
        )
        Button(
            onClick = onAdd,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Matcha)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = CreamWhite)
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(R.string.fab_add_habit),
                color = CreamWhite,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

fun calculateDailyProgress(habits: List<HabitWithSubTasks>): Float {
    val total = habits.sumOf { it.subTasks.size }
    val completed = habits.sumOf { habit -> habit.subTasks.count { it.isDone } }
    return if (total == 0) 0f else completed.toFloat() / total
}
