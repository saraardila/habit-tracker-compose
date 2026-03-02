package com.nawin.habittracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nawin.habittracker.ui.components.AnimatedCircularProgress
import com.nawin.habittracker.ui.viewmodel.HabitViewModel

@Composable
fun StatsScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {

    val habits by viewModel.habits.collectAsState()

    val totalTasks = habits.sumOf { it.subTasks.size }
    val completed = habits.sumOf { it.subTasks.count { it.isDone } }

    val progress =
        if (totalTasks == 0) 0f else completed.toFloat() / totalTasks

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
            "Your Progress 🌿",
            style = MaterialTheme.typography.headlineMedium
        )

        AnimatedCircularProgress(progress)
    }
}