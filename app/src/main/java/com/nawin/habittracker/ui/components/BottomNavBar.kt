package com.nawin.habittracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*

sealed class NavScreen(
    val route: String,
    val icon: ImageVector,
    val labelRes: Int
) {
    object Home     : NavScreen("home",     Icons.Default.Home,        R.string.nav_home)
    object Calendar : NavScreen("calendar", Icons.Default.CalendarMonth, R.string.nav_calendar)
    object Badges   : NavScreen("badges",   Icons.Default.EmojiEvents,  R.string.nav_badges)
    object Stats    : NavScreen("stats",    Icons.Default.BarChart,     R.string.nav_stats)
    object Settings : NavScreen("settings", Icons.Default.Settings,     R.string.nav_settings)
}

@Composable
fun BottomNavBar(navController: NavController) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        NavScreen.Home,
        NavScreen.Calendar,
        NavScreen.Badges,
        NavScreen.Stats,
        NavScreen.Settings
    )

    NavigationBar(
        containerColor = BabyPinkLight,
        tonalElevation = 0.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        items.forEach { screen ->
            NavItem(
                icon = screen.icon,
                label = stringResource(screen.labelRes),
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.NavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "navScale"
    )

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Box(contentAlignment = Alignment.Center) {
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(48.dp, 32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(BabyPink.copy(alpha = 0.4f))
                    )
                }
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (selected) MatchaDark else MatchaDark.copy(alpha = 0.4f),
                    modifier = Modifier.scale(scale)
                )
            }
        },
        label = {
            Text(
                text = label,
                color = if (selected) MatchaDark else MatchaDark.copy(alpha = 0.4f),
                style = MaterialTheme.typography.labelSmall
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = androidx.compose.ui.graphics.Color.Transparent
        )
    )
}