package com.nawin.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.airbnb.lottie.compose.*
import com.nawin.habittracker.R
import com.nawin.habittracker.data.preferences.ALL_PETS
import com.nawin.habittracker.ui.theme.*
import com.nawin.habittracker.ui.viewmodel.HabitViewModel
import com.nawin.habittracker.ui.viewmodel.PetViewModel

data class Badge(
    val emoji: String,
    val title: String,
    val description: String,
    val requiredStreak: Int
)

@Composable
fun BadgesScreen(
    habitViewModel: HabitViewModel = hiltViewModel(),
    petViewModel: PetViewModel = hiltViewModel()
) {
    val habits by habitViewModel.habits.collectAsState()
    val maxStreak = habits.maxOfOrNull { it.habit.currentStreak } ?: 0
    val activePetIndex by petViewModel.activePetIndex.collectAsState()
    val unlockedMask by petViewModel.unlockedMask.collectAsState()

    // Desbloquear mascotas según streak
    LaunchedEffect(maxStreak) {
        petViewModel.unlockPetIfEligible(maxStreak)
    }

    val badges = listOf(
        Badge("🌱", "First Step",    "Complete your first habit",  1),
        Badge("✨", "7 Day Star",    "7 day streak",               7),
        Badge("🌸", "2 Week Bloom",  "14 day streak",              14),
        Badge("🍵", "Matcha Master", "21 day streak",              21),
        Badge("🔥", "On Fire",       "30 day streak",              30),
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.nav_badges),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )

            // Sección mascotas
            Text(
                text = stringResource(R.string.pets_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ALL_PETS.forEach { pet ->
                    val isUnlocked = petViewModel.isPetUnlocked(pet.index, unlockedMask)
                    val isActive = activePetIndex == pet.index

                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(pet.normalRes)
                    )
                    val lottieProgress by animateLottieCompositionAsState(
                        composition = composition,
                        isPlaying = isUnlocked,
                        iterations = LottieConstants.IterateForever
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isActive) BabyPink.copy(alpha = 0.5f)
                                else BabyPinkLight
                            )
                            .border(
                                width = if (isActive) 2.dp else 0.dp,
                                color = if (isActive) Matcha else androidx.compose.ui.graphics.Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable(enabled = isUnlocked) {
                                petViewModel.setActivePet(pet.index)
                            }
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isUnlocked) {
                            LottieAnimation(
                                composition = composition,
                                progress = { lottieProgress },
                                modifier = Modifier.size(80.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier.size(80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🔒", fontSize = 36.sp)
                            }
                        }

                        Text(
                            text = "${pet.emoji} ${pet.name}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) MatchaDark else MatchaDark.copy(alpha = 0.4f)
                        )

                        if (!isUnlocked) {
                            Text(
                                text = "🔥 ${pet.requiredStreak} ${stringResource(R.string.streak_days)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MatchaDark.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        } else if (isActive) {
                            Text(
                                text = stringResource(R.string.pet_active),
                                style = MaterialTheme.typography.labelSmall,
                                color = Matcha,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.pet_tap_to_select),
                                style = MaterialTheme.typography.labelSmall,
                                color = MatchaDark.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = BabyPink.copy(alpha = 0.5f))

            // Sección badges
            Text(
                text = stringResource(R.string.badges_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )

            val unlocked = badges.count { maxStreak >= it.requiredStreak }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(BabyPinkLight, MatchaLight)))
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

            // Grid badges
            badges.forEach { badge ->
                val isBadgeUnlocked = maxStreak >= badge.requiredStreak
                BadgeCard(badge = badge, isUnlocked = isBadgeUnlocked)
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
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (isUnlocked) badge.emoji else "🔒",
                fontSize = 32.sp
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) MatchaDark else MatchaDark.copy(alpha = 0.4f)
                )
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isUnlocked) MatchaDark.copy(alpha = 0.7f) else MatchaDark.copy(alpha = 0.3f)
                )
                if (!isUnlocked) {
                    Text(
                        text = "🔥 ${badge.requiredStreak} ${stringResource(R.string.streak_days)} needed",
                        style = MaterialTheme.typography.labelSmall,
                        color = BabyPinkDark.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}