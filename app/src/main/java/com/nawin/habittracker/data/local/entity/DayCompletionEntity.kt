package com.nawin.habittracker.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_completions")
data class DayCompletionEntity(
    @PrimaryKey
    val date: String, // formato "2026-03-01"
    val completionRate: Float // 0f a 1f
)