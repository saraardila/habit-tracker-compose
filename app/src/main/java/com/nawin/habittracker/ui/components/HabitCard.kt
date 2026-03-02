package com.nawin.habittracker.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nawin.habittracker.R
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.data.local.entity.SubTaskEntity
import com.nawin.habittracker.ui.theme.BabyPink
import com.nawin.habittracker.ui.theme.BabyPinkLight
import com.nawin.habittracker.ui.theme.Matcha
import com.nawin.habittracker.ui.theme.MatchaDark
import com.nawin.habittracker.ui.theme.MatchaLight
import kotlinx.coroutines.delay

@Composable
fun HabitCard(
    habitWithSubTasks: HabitWithSubTasks,
    onToggle: (SubTaskEntity) -> Unit,
    onRenameHabit: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var editing by remember { mutableStateOf(false) }
    var titleText by remember { mutableStateOf(habitWithSubTasks.habit.title) }

    val progress = if (habitWithSubTasks.subTasks.isEmpty()) 0f
    else habitWithSubTasks.subTasks.count { it.isDone }.toFloat() / habitWithSubTasks.subTasks.size

    // Celebración al completar
    var showCelebration by remember { mutableStateOf(false) }
    LaunchedEffect(progress) {
        if (progress == 1f) {
            showCelebration = true
            delay(2000L)
            showCelebration = false
        } else {
            showCelebration = false // 👈 resetea si desmarcas
        }
    }

    // Lottie celebración
    val celebrationComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.celebration)
    )
    val celebrationLottieProgress by animateLottieCompositionAsState(
        composition = celebrationComposition,
        isPlaying = showCelebration,
        restartOnPlay = true
    )

    // Entrada animada de la card
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + slideInVertically(
            animationSpec = tween(400),
            initialOffsetY = { it / 2 }
        )
    ) {
        Box(modifier = modifier.fillMaxWidth()) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BabyPinkLight
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Título + delete
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (editing) {
                            OutlinedTextField(
                                value = titleText,
                                onValueChange = { titleText = it },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        onRenameHabit(titleText)
                                        editing = false
                                    }) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Matcha
                                        )
                                    }
                                }
                            )
                        } else {
                            Text(
                                text = habitWithSubTasks.habit.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MatchaDark,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { editing = true }
                            )
                            IconButton(onClick = onDelete) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = BabyPink
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Subtasks con animación por cada una
                    habitWithSubTasks.subTasks.forEach { sub ->
                        AnimatedSubTask(sub = sub, onToggle = onToggle)
                    }

                    Spacer(Modifier.height(12.dp))

                    // Barra de progreso con gradiente
                    val animatedProgress by animateFloatAsState(
                        targetValue = progress,
                        animationSpec = tween(600),
                        label = "progress"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MatchaLight.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(BabyPink, Matcha)
                                    )
                                )
                        )
                    }

                    // % texto
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MatchaDark.copy(alpha = 0.6f)
                    )
                }
            }

            // Lottie celebración encima de la card
            AnimatedVisibility(
                visible = showCelebration,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(300)),
                modifier = Modifier
                    .matchParentSize()
                    .zIndex(1f)
            ) {
                if (showCelebration) {
                    LottieAnimation(
                        composition = celebrationComposition,
                        progress = { celebrationLottieProgress },
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedSubTask(
    sub: SubTaskEntity,
    onToggle: (SubTaskEntity) -> Unit,
) {
    // Scale del checkbox al hacer toggle
    var justToggled by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (justToggled) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "checkScale"
    )

    LaunchedEffect(justToggled) {
        if (justToggled) {
            delay(200L)
            justToggled = false
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                justToggled = true
                onToggle(sub)
            }
            .padding(vertical = 4.dp)
    ) {
        // Lottie check o checkbox según estado
        Box(modifier = Modifier.scale(scale)) {
            Checkbox(
                checked = sub.isDone,
                onCheckedChange = {
                    justToggled = true
                    onToggle(sub)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Matcha,
                    uncheckedColor = BabyPink,
                    checkmarkColor = Color.White
                )
            )
        }

        Spacer(Modifier.width(8.dp))

        // Texto con animación de tachado
        AnimatedContent(
            targetState = sub.isDone,
            transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
            label = "subtaskText"
        ) { isDone ->
            Text(
                text = sub.title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDone) MatchaDark.copy(alpha = 0.4f) else MatchaDark,
                textDecoration = if (isDone) TextDecoration.LineThrough else null
            )
        }
    }
}