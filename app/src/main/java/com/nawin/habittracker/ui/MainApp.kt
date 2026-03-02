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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

import com.nawin.habittracker.ui.components.AppTopBar
import com.nawin.habittracker.ui.components.BottomNavBar
import com.nawin.habittracker.ui.screens.HabitScreen
import com.nawin.habittracker.ui.screens.StatsScreen
import com.nawin.habittracker.ui.theme.HabitTrackerTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainApp() {

    var darkMode by rememberSaveable { mutableStateOf(false) }
    val navController = rememberAnimatedNavController()

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

            AnimatedNavHost(
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
                composable("home") { HabitScreen() }
                composable("stats") { StatsScreen() }
            }
        }
    }
}