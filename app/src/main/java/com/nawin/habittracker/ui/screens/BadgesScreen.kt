package com.nawin.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*
import com.nawin.habittracker.ui.viewmodel.HabitViewModel

data class Badge(
    val emoji: String,
    val title: String,
    val description: String,
    val requiredStreak: Int
)

@Composable
fun BadgesScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()
    val maxStreak = habits.maxOfOrNull { it.habit.currentStreak } ?: 0

    val badges = listOf(
        Badge("🌱", "First Step",    "Complete your first habit",  1),
        Badge("✨", "7 Day Star",    "7 day streak",               7),
        Badge("🌸", "2 Week Bloom",  "14 day streak",              14),
        Badge("🍵", "Matcha Master", "21 day streak",              21),
        Badge("🔥", "On Fire",       "30 day streak",              30),
        Badge("🐻", "Cozy Bear",     "Complete all habits today",  0),
        Badge("🌿", "Nature Lover",  "50 day streak",              50),
        Badge("👑", "Habit Queen",   "100 day streak",             100),
    )

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
                text = stringResource(R.string.nav_badges),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )

            // Progreso de badges
            val unlocked = badges.count { maxStreak >= it.requiredStreak }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(listOf(BabyPinkLight, MatchaLight))
                    )
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "$unlocked/${badges.size} ${stringResource(R.string.badges_unlocked)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MatchaDark
                    )
                    LinearProgressIndicator(
                        progress = { unlocked.toFloat() / badges.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Matcha,
                        trackColor = CreamWhite
                    )
                }
            }

            // Grid de badges
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badges) { badge ->
                    val isUnlocked = maxStreak >= badge.requiredStreak
                    BadgeCard(badge = badge, isUnlocked = isUnlocked)
                }
            }
        }
    }
}

@Composable
fun BadgeCard(badge: Badge, isUnlocked: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) BabyPinkLight else CreamWhite.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(if (isUnlocked) 4.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (isUnlocked) badge.emoji else "🔒",
                fontSize = 36.sp
            )
            Text(
                text = badge.title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) MatchaDark else MatchaDark.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )
            Text(
                text = badge.description,
                style = MaterialTheme.typography.labelSmall,
                color = if (isUnlocked) MatchaDark.copy(alpha = 0.7f) else MatchaDark.copy(alpha = 0.3f),
                textAlign = TextAlign.Center
            )
        }
    }
}