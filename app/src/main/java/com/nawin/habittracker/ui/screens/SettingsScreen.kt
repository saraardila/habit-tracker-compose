package com.nawin.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*

@Composable
fun SettingsScreen(
    isDark: Boolean = false,
    onToggleDark: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + slideInVertically(tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.nav_settings),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )

            // Osito settings
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(listOf(BabyPinkLight, MatchaLight))
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🐻", fontSize = androidx.compose.ui.unit.TextUnit.Unspecified)
                    Text(
                        text = stringResource(R.string.settings_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MatchaDark
                    )
                }
            }

            // Opciones
            SettingItem(
                icon = Icons.Default.DarkMode,
                title = stringResource(R.string.settings_dark_mode),
                trailing = {
                    Switch(
                        checked = isDark,
                        onCheckedChange = { onToggleDark() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CreamWhite,
                            checkedTrackColor = Matcha,
                            uncheckedThumbColor = CreamWhite,
                            uncheckedTrackColor = BabyPink
                        )
                    )
                }
            )

            SettingItem(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.settings_notifications),
                trailing = {
                    var enabled by remember { mutableStateOf(true) }
                    Switch(
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CreamWhite,
                            checkedTrackColor = Matcha,
                            uncheckedThumbColor = CreamWhite,
                            uncheckedTrackColor = BabyPink
                        )
                    )
                }
            )

            SettingItem(
                icon = Icons.Default.Palette,
                title = stringResource(R.string.settings_theme),
                trailing = {
                    Text(
                        text = "Matcha 🍵",
                        style = MaterialTheme.typography.labelMedium,
                        color = Matcha,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )

            SettingItem(
                icon = Icons.Default.Info,
                title = stringResource(R.string.settings_version),
                trailing = {
                    Text(
                        text = "1.0.0",
                        style = MaterialTheme.typography.labelMedium,
                        color = MatchaDark.copy(alpha = 0.5f)
                    )
                }
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BabyPinkLight)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Matcha)
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MatchaDark,
                fontWeight = FontWeight.Medium
            )
        }
        trailing()
    }
}