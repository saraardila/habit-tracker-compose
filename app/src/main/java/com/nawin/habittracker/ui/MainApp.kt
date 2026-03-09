package com.nawin.habittracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.nawin.habittracker.ui.screens.BadgesScreen
import com.nawin.habittracker.ui.screens.CalendarScreen
import com.nawin.habittracker.ui.screens.DiaryScreen
import com.nawin.habittracker.ui.screens.HabitScreen
import com.nawin.habittracker.ui.screens.SettingsScreen
import com.nawin.habittracker.ui.screens.StatsScreen
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
                composable("stats") { StatsScreen() }
                composable("calendar") { CalendarScreen() }
                composable("badges") { BadgesScreen() }
                composable("diary") { DiaryScreen() }
                composable("settings") {
                    SettingsScreen(
                        isDark = darkMode,
                        onToggleDark = { darkMode = !darkMode }
                    )
                }
            }
        }
    }
}