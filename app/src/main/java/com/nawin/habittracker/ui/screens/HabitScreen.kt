package com.nawin.habittracker.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.ui.components.CreateHabitDialog
import com.nawin.habittracker.ui.components.EmptyState
import com.nawin.habittracker.ui.components.HabitCard
import com.nawin.habittracker.ui.components.HeaderSection
import com.nawin.habittracker.ui.theme.CreamWhite
import com.nawin.habittracker.ui.theme.Matcha
import com.nawin.habittracker.ui.viewmodel.HabitViewModel
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.components.WeeklyHeader


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
) {

    val habits by viewModel.habits.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(20.dp),
                containerColor = Matcha,
                contentColor = CreamWhite,
                icon = {
                    Icon(Icons.Default.Add, contentDescription = null)
                },
                text = {
                    Text(
                        text = stringResource(R.string.fab_add_habit),
                        fontWeight = SemiBold
                    )
                }
            )
        }
    ) { padding ->

        if (habits.isEmpty()) {
            EmptyState { showDialog = true }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    WeeklyHeader(
                        progress = calculateDailyProgress(habits),
                        currentStreak = habits.maxOfOrNull { it.habit.currentStreak } ?: 0,
                        completedDays = emptyMap() // por ahora, luego conectamos con Room
                    )
                }
                items(habits, key = { it.habit.id }) { habitWithSubTasks ->

                    // Swipe para eliminar solo a la derecha
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value: SwipeToDismissBoxValue ->
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
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
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
                            onDelete = { viewModel.deleteHabit(habitWithSubTasks.habit) }
                        )
                    }
                }
            }
        }

        // Crear hábito dialog
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

// Calcula progreso total diario
fun calculateDailyProgress(
    habits: List<HabitWithSubTasks>,
): Float {
    val total = habits.sumOf { it.subTasks.size }
    val completed = habits.sumOf { habit -> habit.subTasks.count { it.isDone } }
    return if (total == 0) 0f else completed.toFloat() / total
}