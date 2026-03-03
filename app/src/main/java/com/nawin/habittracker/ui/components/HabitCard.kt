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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.*
import com.nawin.habittracker.R
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.data.local.entity.SubTaskEntity
import com.nawin.habittracker.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HabitCard(
    habitWithSubTasks: HabitWithSubTasks,
    onToggle: (SubTaskEntity) -> Unit,
    onRenameHabit: (String) -> Unit,
    onRenameSubTask: (SubTaskEntity, String) -> Unit = { _, _ -> }, // 👈 nuevo
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var editing by remember { mutableStateOf(false) }
    var titleText by remember { mutableStateOf(habitWithSubTasks.habit.title) }

    val progress = if (habitWithSubTasks.subTasks.isEmpty()) 0f
    else habitWithSubTasks.subTasks.count { it.isDone }.toFloat() / habitWithSubTasks.subTasks.size

    var showCelebration by remember { mutableStateOf(false) }
    LaunchedEffect(progress) {
        if (progress == 1f) {
            showCelebration = true
            delay(2000L)
            showCelebration = false
        } else {
            showCelebration = false
        }
    }

    val celebrationComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.celebration)
    )
    val celebrationLottieProgress by animateLottieCompositionAsState(
        composition = celebrationComposition,
        isPlaying = showCelebration,
        restartOnPlay = true
    )

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

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
                colors = CardDefaults.cardColors(containerColor = BabyPinkLight)
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
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Matcha,
                                    unfocusedBorderColor = BabyPink,
                                    cursorColor = Matcha
                                ),
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
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = habitWithSubTasks.habit.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MatchaDark,
                                    modifier = Modifier.weight(1f)
                                )
                                // Icono editar título más sutil
                                IconButton(
                                    onClick = { editing = true },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = MatchaDark.copy(alpha = 0.3f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
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

                    // Subtasks
                    habitWithSubTasks.subTasks.forEach { sub ->
                        AnimatedSubTask(
                            sub = sub,
                            onToggle = onToggle,
                            onRename = { newTitle ->
                                onRenameSubTask(sub, newTitle)
                            }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Barra progreso
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

                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MatchaDark.copy(alpha = 0.6f)
                    )
                }
            }

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
    onRename: (String) -> Unit = {}
) {
    var editingSubTask by remember { mutableStateOf(false) }
    var subTaskText by remember { mutableStateOf(sub.title) }

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
            .padding(vertical = 4.dp)
    ) {
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

        Spacer(Modifier.width(4.dp))

        if (editingSubTask) {
            // Campo edición subtarea
            OutlinedTextField(
                value = subTaskText,
                onValueChange = { subTaskText = it },
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Matcha,
                    unfocusedBorderColor = BabyPink,
                    cursorColor = Matcha
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        onRename(subTaskText)
                        editingSubTask = false
                    }) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Matcha,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
        } else {
            // Texto normal + icono editar sutil
            AnimatedContent(
                targetState = sub.isDone,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                label = "subtaskText",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        justToggled = true
                        onToggle(sub)
                    }
            ) { isDone ->
                Text(
                    text = sub.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDone) MatchaDark.copy(alpha = 0.4f) else MatchaDark,
                    textDecoration = if (isDone) TextDecoration.LineThrough else null
                )
            }

            // Botón editar subtarea sutil
            IconButton(
                onClick = { editingSubTask = true },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    tint = MatchaDark.copy(alpha = 0.25f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}