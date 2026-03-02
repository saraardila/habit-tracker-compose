package com.nawin.habittracker.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onToggleDark: () -> Unit,
    isDark: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(BabyPinkLight, MatchaLight)
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.topbar_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MatchaDark
                )
            },
            actions = {
                IconButton(onClick = onToggleDark) {
                    AnimatedContent(
                        targetState = isDark,
                        transitionSpec = {
                            fadeIn(tween(300)) + scaleIn(tween(300)) togetherWith
                                    fadeOut(tween(300)) + scaleOut(tween(300))
                        },
                        label = "darkToggle"
                    ) { dark ->
                        Icon(
                            imageVector = if (dark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = MatchaDark
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
    }
}