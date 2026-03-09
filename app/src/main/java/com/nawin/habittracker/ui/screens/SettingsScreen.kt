package com.nawin.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*
import com.nawin.habittracker.ui.viewmodel.PetViewModel

@Composable
fun SettingsScreen(
    isDark: Boolean = false,
    onToggleDark: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }

    val petViewModel: PetViewModel = hiltViewModel()
    val selectedShortcuts by petViewModel.selectedShortcuts.collectAsState()

    val allShortcuts = listOf(
        "calendar" to "📅 Calendar",
        "stats"    to "📊 Stats",
        "diary"    to "📓 Diary",
        "badges"   to "🏆 Badges"
    )

    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + slideInVertically(tween(500))
    ) {
        // Cambia el Column por uno con scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
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

            Text(
                text = stringResource(R.string.settings_shortcuts),
                style = MaterialTheme.typography.labelLarge,
                color = MatchaDark.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BabyPinkLight)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    allShortcuts.forEach { (route, label) ->
                        val isSelected = route in selectedShortcuts
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) BabyPink.copy(alpha = 0.3f)
                                    else Color.Transparent
                                )
                                .clickable {
                                    val current = selectedShortcuts.toMutableList()
                                    if (isSelected && current.size > 1) {
                                        current.remove(route)
                                    } else if (!isSelected && current.size < 4) {
                                        current.add(route)
                                    }
                                    petViewModel.saveShortcuts(current)
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MatchaDark
                            )
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Matcha,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = stringResource(R.string.settings_shortcuts_hint),
                        style = MaterialTheme.typography.labelSmall,
                        color = MatchaDark.copy(alpha = 0.4f)
                    )
                }
            }

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