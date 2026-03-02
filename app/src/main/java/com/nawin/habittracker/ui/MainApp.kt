package com.nawin.habittracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nawin.habittracker.ui.components.AppTopBar
import com.nawin.habittracker.ui.components.BottomNavBar
import com.nawin.habittracker.ui.screens.HabitScreen
import com.nawin.habittracker.ui.screens.StatsScreen
import com.nawin.habittracker.ui.theme.HabitTrackerTheme

@Composable
fun MainApp() {

    var darkMode by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()

    HabitTrackerTheme(darkTheme = darkMode) {

        Scaffold(
            topBar = {
                AppTopBar(
                    onToggleDark = { darkMode = !darkMode }
                )
            },
            bottomBar = {
                BottomNavBar(navController)
            }
        ) { padding ->

            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") { HabitScreen() }
                composable("stats") { StatsScreen() }
            }
        }
    }
}