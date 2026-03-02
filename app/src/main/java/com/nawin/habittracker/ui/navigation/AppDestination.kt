package com.nawin.habittracker.ui.navigation

sealed class AppDestination(val route: String) {
    object Home : AppDestination("home")
    object Stats : AppDestination("stats")
}