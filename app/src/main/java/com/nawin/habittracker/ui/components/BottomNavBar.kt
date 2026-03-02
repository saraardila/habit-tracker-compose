package com.nawin.habittracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nawin.habittracker.ui.theme.*

@Composable
fun BottomNavBar(navController: NavController) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = BabyPinkLight,
        tonalElevation = 0.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavItem(
            icon = Icons.Default.Home,
            label = "Home",
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
        NavItem(
            icon = Icons.Default.BarChart,
            label = "Stats",
            selected = currentRoute == "stats",
            onClick = {
                navController.navigate("stats") {
                    popUpTo("home") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
private fun RowScope.NavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Animación de escala al seleccionar
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
                // Pill de fondo cuando está seleccionado
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