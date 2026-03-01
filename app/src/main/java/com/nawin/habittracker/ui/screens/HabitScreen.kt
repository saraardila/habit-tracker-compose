package com.nawin.habittracker.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.ui.components.CreateHabitDialog
import com.nawin.habittracker.ui.components.EmptyState
import com.nawin.habittracker.ui.components.HabitCard
import com.nawin.habittracker.ui.components.HeaderSection
import com.nawin.habittracker.ui.viewmodel.HabitViewModel

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        if (habits.isEmpty()) {
            EmptyState { showDialog = true }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    HeaderSection(progress = calculateDailyProgress(habits))
                }
                items(habits) { habitWithSubTasks ->
                    HabitCard(
                        habitWithSubTasks = habitWithSubTasks,
                        onToggle = viewModel::toggleSubTask,
                        onRenameHabit = { viewModel.updateHabitTitle(habitWithSubTasks.habit, it) },
                        onDelete = { viewModel.deleteHabit(habitWithSubTasks.habit)
                        }
                    )
                }
            }
        }

        // El dialog va AQUÍ, dentro del Scaffold pero fuera del if/else
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

fun calculateDailyProgress(habits: List<HabitWithSubTasks>): Float {
    val total = habits.sumOf { it.subTasks.size }
    val completed = habits.sumOf { habit ->
        habit.subTasks.count { it.isDone }
    }

    return if (total == 0) 0f
    else completed.toFloat() / total
}