package com.nawin.habittracker.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nawin.habittracker.R
import com.nawin.habittracker.ui.theme.*

@Composable
fun CreateHabitDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, subtasks: List<String>) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var subtasks by remember { mutableStateOf(listOf("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = BabyPinkLight,
        title = {
            Text(
                text = stringResource(R.string.dialog_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {

                // Campo título
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = {
                        Text(
                            stringResource(R.string.habit_title_hint),
                            color = MatchaDark.copy(alpha = 0.4f)
                        )
                    },
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Matcha,
                        unfocusedBorderColor = BabyPink,
                        focusedLabelColor = Matcha,
                        cursorColor = Matcha
                    )
                )

                // Separador con gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(BabyPink, Matcha)
                            )
                        )
                )

                Text(
                    text = stringResource(R.string.checklist_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MatchaDark,
                    fontWeight = FontWeight.SemiBold
                )

                // Subtasks — BUG ARREGLADO AQUÍ
                subtasks.forEachIndexed { index, value ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(200)) + slideInVertically(tween(200))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🌿",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            OutlinedTextField(
                                value = value,
                                onValueChange = { newValue ->
                                    // ✅ ARREGLADO: usa newValue, no it
                                    subtasks = subtasks.toMutableList().also { list ->
                                        list[index] = newValue
                                    }
                                },
                                placeholder = {
                                    Text(
                                        "${stringResource(R.string.task_hint)} ${index + 1}",
                                        color = MatchaDark.copy(alpha = 0.4f)
                                    )
                                },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Matcha,
                                    unfocusedBorderColor = BabyPink,
                                    cursorColor = Matcha
                                )
                            )
                            // Botón eliminar tarea
                            if (subtasks.size > 1) {
                                IconButton(onClick = {
                                    subtasks = subtasks.toMutableList()
                                        .also { it.removeAt(index) }
                                }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        tint = BabyPinkDark
                                    )
                                }
                            }
                        }
                    }
                }

                // Añadir tarea
                TextButton(
                    onClick = { subtasks = subtasks + "" },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Matcha
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        stringResource(R.string.add_task),
                        color = Matcha,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(title, subtasks.filter { it.isNotBlank() })
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Matcha,
                    contentColor = CreamWhite
                )
            ) {
                Text(
                    stringResource(R.string.create),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    stringResource(R.string.cancel),
                    color = MatchaDark.copy(alpha = 0.6f)
                )
            }
        }
    )
}