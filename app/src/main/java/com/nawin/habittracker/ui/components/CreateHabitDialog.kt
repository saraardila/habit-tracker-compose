package com.nawin.habittracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                "Create a new habit 🌾",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Morning routine") },
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    "Checklist",
                    style = MaterialTheme.typography.labelLarge
                )

                subtasks.forEachIndexed { index, value ->

                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            subtasks = subtasks.toMutableList().also {
                                it[index] = it.toString()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                TextButton(
                    onClick = { subtasks = subtasks + "" }
                ) {
                    Text("+ Add another task")
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
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Create Habit")
            }
        }
    )
}