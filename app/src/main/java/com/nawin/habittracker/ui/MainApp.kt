package com.nawin.habittracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nawin.habittracker.ui.components.AppTopBar
import com.nawin.habittracker.ui.components.BottomNavBar
import com.nawin.habittracker.ui.screens.*
import com.nawin.habittracker.ui.theme.HabitTrackerTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp() {

    var darkMode by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()

    HabitTrackerTheme(darkTheme = darkMode) {
        Scaffold(
            topBar = {
                AppTopBar(
                    onToggleDark = { darkMode = !darkMode },
                    isDark = darkMode
                )
            },
            bottomBar = {
                BottomNavBar(navController)
            }
        ) { padding ->

            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(padding),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(350)
                    ) + fadeIn(tween(350))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(350)
                    ) + fadeOut(tween(350))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(350)
                    ) + fadeIn(tween(350))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(350)
                    ) + fadeOut(tween(350))
                }
            ) {
                composable("home") {
                    HabitScreen(navController = navController)
                }
//                composable("stats")    { StatsScreen() }
//                composable("calendar") { CalendarScreen() }
                composable("badges")   { BadgesScreen() }
                composable("diary") { DiaryScreen() }
                composable("settings") {
                    SettingsScreen(          // 👈 aquí le pasas isDark y onToggleDark
                        isDark = darkMode,
                        onToggleDark = { darkMode = !darkMode }
                    )
                }
            }
        }
    }
}