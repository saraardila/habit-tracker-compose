package com.nawin.habittracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.data.local.entity.SubTaskEntity

@Composable
fun HabitCard(
    habitWithSubTasks: HabitWithSubTasks,
    onToggle: (SubTaskEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    val habit = habitWithSubTasks.habit

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Título del hábito
            Text(
                text = habit.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Checklist de subtareas
            habitWithSubTasks.subTasks.forEach { sub ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggle(sub) }
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = sub.isDone,
                        onCheckedChange = { onToggle(sub) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = sub.title,
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (sub.isDone) TextDecoration.LineThrough else null,
                        color = if (sub.isDone) colorScheme.onSurface.copy(alpha = 0.5f)
                        else colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de progreso
            val progress = if (habitWithSubTasks.subTasks.isEmpty()) 0f
            else habitWithSubTasks.subTasks.count { it.isDone }
                .toFloat() / habitWithSubTasks.subTasks.size

            val animatedProgress by animateFloatAsState(progress)

            LinearProgressIndicator(
                progress = { animatedProgress }, // ⚡ lambda
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = colorScheme.primary,
                trackColor = colorScheme.onSurface.copy(alpha = 0.1f)
            )
        }
    }
}