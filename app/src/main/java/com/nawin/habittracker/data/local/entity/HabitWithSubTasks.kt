package com.nawin.habittracker.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithSubTasks(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val subTasks: List<SubTaskEntity>
)