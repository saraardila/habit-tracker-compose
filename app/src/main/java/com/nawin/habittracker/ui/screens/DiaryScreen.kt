package com.nawin.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nawin.habittracker.R
import com.nawin.habittracker.data.local.entity.DiaryEntity
import com.nawin.habittracker.ui.theme.*
import com.nawin.habittracker.ui.viewmodel.DiaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    viewModel: DiaryViewModel = hiltViewModel()
) {
    val entries by viewModel.entries.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val moodEmojis = listOf("😊", "🌸", "😴", "💪", "🍵", "😔", "✨", "🐻")

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + slideInVertically(tween(500))
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = Matcha,
                    contentColor = CreamWhite,
                    icon = { Icon(Icons.Default.Add, null) },
                    text = {
                        Text(
                            stringResource(R.string.diary_add),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.nav_diary),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MatchaDark
                )

                if (entries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(listOf(BabyPinkLight, MatchaLight))
                            )
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("📓", fontSize = 48.sp)
                            Text(
                                text = stringResource(R.string.diary_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MatchaDark.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(entries, key = { it.id }) { entry ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        viewModel.deleteEntry(entry)
                                        true
                                    } else false
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(BabyPink.copy(alpha = 0.8f))
                                            .padding(end = 24.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = MatchaDark
                                        )
                                    }
                                },
                                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            ) {
                                DiaryEntryCard(entry = entry)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        DiaryEntryDialog(
            moodEmojis = moodEmojis,
            onDismiss = { showDialog = false },
            onSave = { text, emoji ->
                viewModel.addEntry(text, emoji)
                showDialog = false
            }
        )
    }
}

@Composable
fun DiaryEntryCard(entry: DiaryEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BabyPinkLight),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(entry.emoji, fontSize = 28.sp)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MatchaDark.copy(alpha = 0.5f)
                )
                Text(
                    text = entry.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MatchaDark
                )
            }
        }
    }
}

@Composable
fun DiaryEntryDialog(
    moodEmojis: List<String>,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("😊") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = BabyPinkLight,
        title = {
            Text(
                text = stringResource(R.string.diary_new_entry),
                fontWeight = FontWeight.Bold,
                color = MatchaDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.diary_mood),
                    style = MaterialTheme.typography.labelLarge,
                    color = MatchaDark
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    moodEmojis.forEach { emoji ->
                        TextButton(
                            onClick = { selectedEmoji = emoji },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (selectedEmoji == emoji) BabyPink
                                    else androidx.compose.ui.graphics.Color.Transparent
                                )
                        ) {
                            Text(emoji, fontSize = 20.sp)
                        }
                    }
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(
                            stringResource(R.string.diary_placeholder),
                            color = MatchaDark.copy(alpha = 0.4f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Matcha,
                        unfocusedBorderColor = BabyPink,
                        cursorColor = Matcha
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (text.isNotBlank()) onSave(text, selectedEmoji) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Matcha)
            ) {
                Text(stringResource(R.string.create), color = CreamWhite)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = MatchaDark.copy(alpha = 0.6f))
            }
        }
    )
}