package com.nawin.habittracker.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import com.nawin.habittracker.R
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.ui.components.CreateHabitDialog
import com.nawin.habittracker.ui.components.HabitCard
import com.nawin.habittracker.ui.components.WeeklyHeader
import com.nawin.habittracker.ui.theme.BabyPink
import com.nawin.habittracker.ui.theme.CreamWhite
import com.nawin.habittracker.ui.theme.Matcha
import com.nawin.habittracker.ui.theme.MatchaDark
import com.nawin.habittracker.ui.viewmodel.HabitViewModel
import com.nawin.habittracker.ui.viewmodel.PetViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    navController: NavController? = null,
) {
    val habits by viewModel.habits.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val petViewModel: PetViewModel = hiltViewModel()
    val selectedShortcuts by petViewModel.selectedShortcuts.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        // FAB redondo con emoji
        floatingActionButton = {
            AnimatedVisibility(
                visible = habits.isNotEmpty(),
                enter = scaleIn(tween(200)) + fadeIn(tween(200)),
                exit = scaleOut(tween(200)) + fadeOut(tween(200))
            ) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    shape = CircleShape,
                    containerColor = BabyPink,
                    contentColor = CreamWhite,
                    modifier = Modifier.size(64.dp),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Text("🌸", fontSize = 28.sp)
                }
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
                QuickAccessRow(
                    navController = navController,
                    selectedShortcuts = selectedShortcuts
                )
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
                            onRenameHabit = {
                                viewModel.updateHabitTitle(
                                    habitWithSubTasks.habit,
                                    it
                                )
                            },
                            onRenameSubTask = { subTask, newTitle ->
                                viewModel.renameSubTask(
                                    subTask,
                                    newTitle
                                )
                            },
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

// Accesos rápidos
@Composable
fun QuickAccessRow(navController: NavController?, selectedShortcuts: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        selectedShortcuts.forEach { route ->
            val config = shortcutConfig(route)
            if (config != null) {
                CircularShortcut(
                    icon = config.first,
                    color = config.second,
                    onClick = {
                        navController?.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun CircularShortcut(
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
    }
}

fun shortcutConfig(route: String): Pair<ImageVector, Color>? = when (route) {
    "calendar" -> Pair(Icons.Default.CalendarMonth, Matcha)
    "stats" -> Pair(Icons.Default.BarChart, Color(0xFFE88FA0))
    "diary" -> Pair(Icons.Default.AutoStories, Color(0xFFB39DDB))
    "badges" -> Pair(Icons.Default.EmojiEvents, Color(0xFFFFB347))
    else -> null
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
