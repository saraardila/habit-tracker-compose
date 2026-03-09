package com.nawin.habittracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Matcha,
    secondary = BabyPink,
    background = CreamWhite,
    surface = Color.White,
    surfaceVariant = BabyPinkLight,
    onPrimary = Color.White,
    onSecondary = SoftBrown,
    onBackground = SoftBrown,
    onSurface = SoftBrown,
    onSurfaceVariant = MatchaDark
)

private val DarkColorScheme = darkColorScheme(
    primary = Matcha,
    secondary = BabyPinkDark,
    background = MatchaDarkBg,
    surface = MatchaDarkSurface,
    surfaceVariant = Color(0xFF3A2A30),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = BabyPinkLight
)

@Composable
fun HabitTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}