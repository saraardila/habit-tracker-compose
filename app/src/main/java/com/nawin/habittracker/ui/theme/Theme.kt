package com.nawin.habittracker.ui.theme




import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val LightColorScheme = lightColorScheme(
//    primary = PrimarySage,
//    secondary = AccentBeige,
//    background = BackgroundLight,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onBackground = TextPrimary,
//    onSurface = TextPrimary
//)

//private val DarkColorScheme = darkColorScheme(
//    primary = PrimarySageDark,
//    secondary = AccentBeigeDark,
//    background = BackgroundDark,
//    surface = Color(0xFF2A2A2A),
//    onPrimary = Color.White,
//    onBackground = Color.White,
//    onSurface = Color.White
//)

private val LightColors = lightColorScheme(
    primary = Color(0xFF7C9A92),
    secondary = Color(0xFFD8CFC4),
    background = Color(0xFFF6F1EB),
    surface = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9BBFAF),
    background = Color(0xFF1B1B1B),
    surface = Color(0xFF242424)
)
@Composable
fun HabitTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}