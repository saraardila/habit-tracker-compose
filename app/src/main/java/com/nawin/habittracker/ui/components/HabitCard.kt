package com.nawin.habittracker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.data.local.entity.SubTaskEntity
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

    // Animación de celebración
    var showCelebration by remember { mutableStateOf(false) }
    val progress = if (habitWithSubTasks.subTasks.isEmpty()) 0f
    else habitWithSubTasks.subTasks.count { it.isDone }.toFloat() / habitWithSubTasks.subTasks.size

    if (progress == 1f && !showCelebration) {
        LaunchedEffect(Unit) {
            showCelebration = true
            delay(2000L)
            showCelebration = false
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFEEFFF) // fondo pastel lilac
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Título
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
                            trailingIcon = {
                                IconButton(onClick = {
                                    onRenameHabit(titleText)
                                    editing = false
                                }) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                }
                            }
                        )
                    } else {
                        Text(
                            text = habitWithSubTasks.habit.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { editing = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Subtasks
                habitWithSubTasks.subTasks.forEach { sub ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggle(sub) }
                            .padding(vertical = 2.dp)
                    ) {
                        Checkbox(
                            checked = sub.isDone,
                            onCheckedChange = { onToggle(sub) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(sub.title)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Barra de progreso
                val animatedProgress by animateFloatAsState(progress)
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFB39DDB), // pastel lilac
                    trackColor = Color(0xFFEDE7F6)
                )
            }
        }

        // Celebración Lottie sobre la card
        AnimatedVisibility(
            visible = showCelebration,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .matchParentSize()
                .zIndex(1f)
        ) {
            CelebrationAnimation()
        }
    }
}